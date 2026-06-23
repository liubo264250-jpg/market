package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.infrastructure.persistent.dao.RaffleActivityCountMapper;
import com.liubo.infrastructure.persistent.dao.RaffleActivityMapper;
import com.liubo.infrastructure.persistent.dao.RaffleActivitySkuMapper;
import com.liubo.infrastructure.persistent.po.RaffleActivity;
import com.liubo.infrastructure.persistent.po.RaffleActivityCount;
import com.liubo.infrastructure.persistent.po.RaffleActivitySku;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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


    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuMapper.selectOne(Wrappers.<RaffleActivitySku>lambdaQuery().eq(RaffleActivitySku::getSku, sku));
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        RaffleActivity raffleActivity = raffleActivityMapper.selectOne(Wrappers.<RaffleActivity>lambdaQuery().eq(RaffleActivity::getActivityId, activityId));
        return ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(raffleActivity.getState())
                .build();
    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        RaffleActivityCount raffleActivityCount = raffleActivityCountMapper.selectOne(Wrappers.<RaffleActivityCount>lambdaQuery().eq(RaffleActivityCount::getActivityCountId, activityCountId));
        return ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
    }
}
