package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @author 68
 * 2026/6/28 15:56
 */
public interface IRaffleActivityAccountQuotaService {

    String createOrder(SkuRechargeEntity skuRechargeEntity);

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);
}
