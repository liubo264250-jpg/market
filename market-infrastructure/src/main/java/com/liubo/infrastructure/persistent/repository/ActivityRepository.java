package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.activity.event.ActivitySkuStockZeroMessageEvent;
import com.liubo.domain.activity.model.aggregate.CreateOrderAggregate;
import com.liubo.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.liubo.domain.activity.model.entity.*;
import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.liubo.domain.activity.model.valobj.ActivityStateVO;
import com.liubo.domain.activity.model.valobj.UserRaffleOrderStateVO;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.infrastructure.event.EventPublisher;
import com.liubo.infrastructure.persistent.dao.*;
import com.liubo.infrastructure.persistent.po.*;
import com.liubo.infrastructure.persistent.redis.IRedisService;
import com.liubo.types.common.Constants;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.exception.AppException;
import com.liubo.types.utils.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private UserRaffleOrderMapper userRaffleOrderMapper;

    @Resource
    private RaffleActivityAccountDayMapper raffleActivityAccountDayMapper;

    @Resource
    private RaffleActivityAccountMonthMapper raffleActivityAccountMonthMapper;

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
                .state(ActivityStateVO.findByCode(raffleActivity.getState()))
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
    public UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        UserRaffleOrder userRaffleOrder = userRaffleOrderMapper.selectOne(Wrappers.<UserRaffleOrder>lambdaQuery()
                .eq(UserRaffleOrder::getUserId, partakeRaffleActivityEntity.getUserId())
                .eq(UserRaffleOrder::getActivityId, partakeRaffleActivityEntity.getActivityId())
                .eq(UserRaffleOrder::getOrderState, UserRaffleOrderStateVO.create.getCode()));
        return Optional.ofNullable(userRaffleOrder)
                .map(order -> UserRaffleOrderEntity.builder()
                        .userId(order.getUserId())
                        .activityId(order.getActivityId())
                        .activityName(order.getActivityName())
                        .strategyId(order.getStrategyId())
                        .orderId(order.getOrderId())
                        .orderTime(order.getOrderTime())
                        .orderState(UserRaffleOrderStateVO.findByCode(order.getOrderState()))
                        .build())
                .orElse(null);
    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId) {
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountMapper.selectOne(Wrappers.<RaffleActivityAccount>lambdaQuery()
                .eq(RaffleActivityAccount::getUserId, userId)
                .eq(RaffleActivityAccount::getActivityId, activityId));
        return Optional.ofNullable(raffleActivityAccount)
                .map(item -> ActivityAccountEntity.builder()
                        .userId(item.getUserId())
                        .activityId(item.getActivityId())
                        .totalCount(item.getTotalCount())
                        .totalCountSurplus(item.getTotalCountSurplus())
                        .dayCount(item.getDayCount())
                        .dayCountSurplus(item.getDayCountSurplus())
                        .monthCount(item.getMonthCount())
                        .monthCountSurplus(item.getMonthCountSurplus())
                        .build())
                .orElse(null);
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month) {
        RaffleActivityAccountMonth raffleActivityAccountMonth = raffleActivityAccountMonthMapper.selectOne(Wrappers.<RaffleActivityAccountMonth>lambdaQuery()
                .eq(RaffleActivityAccountMonth::getUserId, userId)
                .eq(RaffleActivityAccountMonth::getActivityId, activityId)
                .eq(RaffleActivityAccountMonth::getMonth, month));
        return Optional.ofNullable(raffleActivityAccountMonth)
                .map(item -> ActivityAccountMonthEntity.builder()
                        .userId(item.getUserId())
                        .activityId(item.getActivityId())
                        .month(item.getMonth())
                        .monthCount(item.getMonthCount())
                        .monthCountSurplus(item.getMonthCountSurplus())
                        .build())
                .orElse(null);
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day) {
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayMapper.selectOne(Wrappers.<RaffleActivityAccountDay>lambdaQuery()
                .eq(RaffleActivityAccountDay::getUserId, userId)
                .eq(RaffleActivityAccountDay::getActivityId, activityId)
                .eq(RaffleActivityAccountDay::getDay, day));
        return Optional.ofNullable(raffleActivityAccountDay)
                .map(item -> ActivityAccountDayEntity.builder()
                        .userId(item.getUserId())
                        .activityId(item.getActivityId())
                        .day(item.getDay())
                        .dayCount(item.getDayCount())
                        .dayCountSurplus(item.getDayCountSurplus())
                        .build())
                .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {
        String userId = createPartakeOrderAggregate.getUserId();
        Long activityId = createPartakeOrderAggregate.getActivityId();
        ActivityAccountEntity activityAccountEntity = createPartakeOrderAggregate.getActivityAccountEntity();
        ActivityAccountMonthEntity activityAccountMonthEntity = createPartakeOrderAggregate.getActivityAccountMonthEntity();
        ActivityAccountDayEntity activityAccountDayEntity = createPartakeOrderAggregate.getActivityAccountDayEntity();
        UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();
        // 1. 更新总账户
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(userId);
        raffleActivityAccount.setActivityId(activityId);
        int totalCount = raffleActivityAccountMapper.updateActivityAccountSubtractionQuota(raffleActivityAccount);
        if (1 != totalCount) {
            log.warn("写入创建参与活动记录，更新总账户额度不足，异常 userId: {} activityId: {}", userId, activityId);
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }
        // 2. 创建或更新日账户，true - 存在则更新，false - 不存在则插入
        if (createPartakeOrderAggregate.isExistAccountMonth()) {
            RaffleActivityAccountDay updateRaffleActivityAccountDay = new RaffleActivityAccountDay();
            updateRaffleActivityAccountDay.setUserId(userId);
            updateRaffleActivityAccountDay.setActivityId(activityId);
            updateRaffleActivityAccountDay.setDay(activityAccountDayEntity.getDay());
            int updateMonthCount = raffleActivityAccountDayMapper.updateActivityAccountMonthSubtractionQuota(updateRaffleActivityAccountDay);
            if (1 != updateMonthCount) {
                // 未更新成功则回滚
                log.warn("写入创建参与活动记录，更新月账户额度不足，异常 userId: {} activityId: {} month: {}", userId, activityId, activityAccountMonthEntity.getMonth());
                throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
            }
        } else {
            RaffleActivityAccountDay saveRaffleActivityAccountDay = new RaffleActivityAccountDay();
            saveRaffleActivityAccountDay.setUserId(activityAccountDayEntity.getUserId());
            saveRaffleActivityAccountDay.setActivityId(activityAccountDayEntity.getActivityId());
            saveRaffleActivityAccountDay.setDay(activityAccountDayEntity.getDay());
            saveRaffleActivityAccountDay.setDayCount(activityAccountDayEntity.getDayCount());
            saveRaffleActivityAccountDay.setDayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1);
            raffleActivityAccountDayMapper.insert(saveRaffleActivityAccountDay);
            // 新创建月账户，则更新总账表中日镜像额度
            RaffleActivityAccount updateActivityAccountDaySurplusImageQuota = new RaffleActivityAccount();
            updateActivityAccountDaySurplusImageQuota.setUserId(userId);
            updateActivityAccountDaySurplusImageQuota.setActivityId(activityId);
            updateActivityAccountDaySurplusImageQuota.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
            raffleActivityAccountMapper.updateActivityAccountDaySurplusImageQuota(updateActivityAccountDaySurplusImageQuota);
        }

        // 3. 创建或更新月账户，true - 存在则更新，false - 不存在则插入
        if (createPartakeOrderAggregate.isExistAccountDay()) {
            RaffleActivityAccountMonth updateRaffleActivityAccountMonth = new RaffleActivityAccountMonth();
            updateRaffleActivityAccountMonth.setUserId(userId);
            updateRaffleActivityAccountMonth.setActivityId(activityId);
            updateRaffleActivityAccountMonth.setMonth(activityAccountMonthEntity.getMonth());
            int updateDayCount = raffleActivityAccountMonthMapper.updateActivityAccountDaySubtractionQuota(updateRaffleActivityAccountMonth);
            if (1 != updateDayCount) {
                // 未更新成功则回滚
                log.warn("写入创建参与活动记录，更新日账户额度不足，异常 userId: {} activityId: {} day: {}", userId, activityId, activityAccountDayEntity.getDay());
                throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
            }
        } else {
            RaffleActivityAccountMonth saveRaffleActivityAccountMonth = new RaffleActivityAccountMonth();
            saveRaffleActivityAccountMonth.setUserId(activityAccountMonthEntity.getUserId());
            saveRaffleActivityAccountMonth.setActivityId(activityAccountMonthEntity.getActivityId());
            saveRaffleActivityAccountMonth.setMonth(activityAccountMonthEntity.getMonth());
            saveRaffleActivityAccountMonth.setMonthCount(activityAccountMonthEntity.getMonthCount());
            saveRaffleActivityAccountMonth.setMonthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1);
            raffleActivityAccountMonthMapper.insert(saveRaffleActivityAccountMonth);
            // 新创建日账户，则更新总账表中月镜像额度
            RaffleActivityAccount updateActivityAccountMonthSurplusImageQuota = new RaffleActivityAccount();
            updateActivityAccountMonthSurplusImageQuota.setUserId(userId);
            updateActivityAccountMonthSurplusImageQuota.setActivityId(activityId);
            updateActivityAccountMonthSurplusImageQuota.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            raffleActivityAccountMapper.updateActivityAccountMonthSurplusImageQuota(updateActivityAccountMonthSurplusImageQuota);
        }

        // 4. 写入参与活动订单
        UserRaffleOrder saveUserRaffleOrder = new UserRaffleOrder();
        saveUserRaffleOrder.setUserId(userRaffleOrderEntity.getUserId());
        saveUserRaffleOrder.setActivityId(userRaffleOrderEntity.getActivityId());
        saveUserRaffleOrder.setActivityName(userRaffleOrderEntity.getActivityName());
        saveUserRaffleOrder.setStrategyId(userRaffleOrderEntity.getStrategyId());
        saveUserRaffleOrder.setOrderId(userRaffleOrderEntity.getOrderId());
        saveUserRaffleOrder.setOrderTime(userRaffleOrderEntity.getOrderTime());
        saveUserRaffleOrder.setOrderState(userRaffleOrderEntity.getOrderState().getCode());
        userRaffleOrderMapper.insert(saveUserRaffleOrder);
    }

    @Override
    public List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId) {
        List<RaffleActivitySku> raffleActivitySkus = raffleActivitySkuMapper.selectList(Wrappers.<RaffleActivitySku>lambdaQuery().eq(RaffleActivitySku::getActivityId, activityId));
        return Optional.ofNullable(raffleActivitySkus)
                .orElse(new ArrayList<>())
                .stream()
                .map(raffleActivitySku -> ActivitySkuEntity.builder()
                        .sku(raffleActivitySku.getSku())
                        .activityId(raffleActivitySku.getActivityId())
                        .activityCountId(raffleActivitySku.getActivityCountId())
                        .stockCount(raffleActivitySku.getStockCount())
                        .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                        .build()).collect(Collectors.toList());

    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId) {
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayMapper.selectOne(Wrappers.<RaffleActivityAccountDay>lambdaQuery()
                .eq(RaffleActivityAccountDay::getActivityId, activityId)
                .eq(RaffleActivityAccountDay::getUserId, userId)
                .eq(RaffleActivityAccountDay::getDay, DateUtils.formatDate(new Date())));
        Integer dayCount = Optional.ofNullable(raffleActivityAccountDay).map(RaffleActivityAccountDay::getDayCount).orElse(0);
        Integer dayCountSurplus = Optional.ofNullable(raffleActivityAccountDay).map(RaffleActivityAccountDay::getDayCountSurplus).orElse(0);
        return dayCount -  dayCountSurplus;
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

        // 账户对象 - 月
        RaffleActivityAccountMonth raffleActivityAccountMonth = new RaffleActivityAccountMonth();
        raffleActivityAccountMonth.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccountMonth.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccountMonth.setMonth(DateUtils.formatMonth(new Date()));
        raffleActivityAccountMonth.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityAccountMonth.setMonthCountSurplus(createOrderAggregate.getMonthCount());

        // 账户对象 - 日
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccountDay.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccountDay.setDay(DateUtils.formatDate(new Date()));
        raffleActivityAccountDay.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccountDay.setDayCountSurplus(createOrderAggregate.getDayCount());
        boolean exists = raffleActivityOrderMapper.exists(Wrappers.<RaffleActivityOrder>lambdaQuery().eq(RaffleActivityOrder::getOutBusinessNo, activityOrderEntity.getOutBusinessNo()));
        if (!exists) {
            raffleActivityOrderMapper.insert(raffleActivityOrder);
        }
        int update = raffleActivityAccountMapper.updateAccountQuota(raffleActivityAccount);
        if (0 == update) {
            raffleActivityAccountMapper.insert(raffleActivityAccount);
        }
        // 4. 更新账户 - 月
        raffleActivityAccountMonthMapper.addAccountQuota(raffleActivityAccountMonth);
        // 5. 更新账户 - 日
        raffleActivityAccountDayMapper.addAccountQuota(raffleActivityAccountDay);
    }
}
