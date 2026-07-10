package com.liubo.domain.activity.service.quota.policy;

import com.liubo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @author 68
 * 2026/7/8 09:25
 */
public interface ITradePolicy {
    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
