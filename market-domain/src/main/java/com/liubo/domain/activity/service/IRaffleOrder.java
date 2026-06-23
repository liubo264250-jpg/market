package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.ActivityOrderEntity;
import com.liubo.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @author 68
 * 2026/6/23 17:32
 */
public interface IRaffleOrder {
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
