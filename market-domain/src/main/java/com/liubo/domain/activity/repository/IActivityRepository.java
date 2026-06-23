package com.liubo.domain.activity.repository;

import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author 68
 * 2026/6/23 17:33
 */
public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
