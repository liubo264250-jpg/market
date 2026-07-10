package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.ActivityAccountEntity;
import com.liubo.domain.activity.model.entity.DeliveryOrderEntity;
import com.liubo.domain.activity.model.entity.SkuRechargeEntity;
import com.liubo.domain.activity.model.entity.UnpaidActivityOrderEntity;

/**
 * @author 68
 * 2026/6/28 15:56
 */
public interface IRaffleActivityAccountQuotaService {
    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    Integer queryRaffleActivityAccountPartakeCount(Long activityId, String userId);

    void updateOrder(DeliveryOrderEntity deliveryOrderEntity);

    UnpaidActivityOrderEntity createOrder(SkuRechargeEntity skuRechargeEntity);
}
