package com.liubo.domain.rebate.service;

import com.liubo.domain.rebate.model.entity.BehaviorEntity;
import com.liubo.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * @author 68
 * 2026/7/4 22:53
 */
public interface IBehaviorRebateService {
    List<String> createOrder(BehaviorEntity behaviorEntity);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo);
}
