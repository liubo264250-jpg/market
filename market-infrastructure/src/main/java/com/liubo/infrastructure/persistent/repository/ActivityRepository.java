package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.liubo.domain.activity.model.aggregate.CreateOrderAggregate;
import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivityOrderEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.infrastructure.event.EventPublisher;
import com.liubo.infrastructure.persistent.dao.*;
import com.liubo.infrastructure.persistent.po.*;
import com.liubo.infrastructure.persistent.redis.IRedisService;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 68
 * 2026/6/23 17:43
 */
@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {

    @Resource
    private RaffleActivitySkuMapper raffleActivitySkuMapper;

    @Resource
    private RaffleActivityMapper raffleActivityMapper;

    @Resource
    private RaffleActivityCountMapper raffleActivityCountMapper;

    @Resource
    private RaffleActivityOrderMapper raffleActivityOrderMapper;

    @Resource
    private RaffleActivityAccountMapper raffleActivityAccountMapper;

    @Resource
    private IRedisService redisService;

    @Resource
    private EventPublisher mqService;

    @Resource
    private ActivitySkuStockZeroMessageEvent activitySkuStockZeroMessageEvent;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        String activitySkuKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        Long cacheSkuStock = redisService.getAtomicLong(activitySkuKey);
        cacheSkuStock = cacheSkuStock != null ? cacheSkuStock : 0L;
        RaffleActivitySku raffleActivitySku = raffleActivitySkuMapper.selectOne(Wrappers.<RaffleActivitySku>lambdaQuery().eq(RaffleActivitySku::getSku, sku));
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(cacheSkuStock.intValue())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        String activityKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        if (redisService.isExists(activityKey)) {
            return redisService.getValue(activityKey);
        }
        RaffleActivity raffleActivity = raffleActivityMapper.selectOne(Wrappers.<RaffleActivity>lambdaQuery().eq(RaffleActivity::getActivityId, activityId));
        ActivityEntity activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(raffleActivity.getState())
                .build();
        redisService.setValue(activityKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        String activityCountKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        if (redisService.isExists(activityCountKey)) {
            return redisService.getValue(activityCountKey);
        }
        RaffleActivityCount raffleActivityCount = raffleActivityCountMapper.selectOne(Wrappers.<RaffleActivityCount>lambdaQuery().eq(RaffleActivityCount::getActivityCountId, activityCountId));
        ActivityCountEntity activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(activityCountKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void cacheActivitySkuStockCount(String cacheKey, Integer stockCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, stockCount);
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(activitySkuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        RaffleActivitySku raffleActivitySku = new RaffleActivitySku();
        raffleActivitySku.setSku(sku);
        raffleActivitySkuMapper.updateActivitySkuStock(raffleActivitySku);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (0 == surplus) {
            // 库存已扣完，发送mq消息
            mqService.publish(activitySkuStockZeroMessageEvent.topic(),
                    activitySkuStockZeroMessageEvent.buildEventMessage(sku));
            return false;
        } else if (surplus < 0) {
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("活动sku库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        RaffleActivitySku raffleActivitySku = new RaffleActivitySku();
        raffleActivitySku.setSku(sku);
        raffleActivitySkuMapper.clearActivitySkuStock(raffleActivitySku);
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_COUNT_QUERY_KEY;
        RBlockingQueue<ActivitySkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<ActivitySkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        // 订单对象
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId(activityOrderEntity.getUserId());
        raffleActivityOrder.setSku(activityOrderEntity.getSku());
        raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
        raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
        raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
        raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
        raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
        raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
        raffleActivityOrder.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityOrder.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityOrder.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

        // 账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());
        raffleActivityOrderMapper.insert(raffleActivityOrder);
        int update = raffleActivityAccountMapper.update(raffleActivityAccount, Wrappers.<RaffleActivityAccount>lambdaUpdate()
                .eq(RaffleActivityAccount::getUserId, createOrderAggregate.getUserId())
                .eq(RaffleActivityAccount::getActivityId, createOrderAggregate.getActivityId()));
        if (0 == update) {
            raffleActivityAccountMapper.insert(raffleActivityAccount);
        }
    }
}
