package com.liubo.domain.activity.service.armory;

/**
 * @author 68
 * 2026/6/26 22:09
 */
public interface IActivityArmory {
    boolean assembleActivitySkuByActivityId(Long activityId);

    boolean assembleActivitySku(Long sku);
}
