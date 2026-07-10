package com.liubo.domain.activity.service.quota.policy.impl;

import com.liubo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.liubo.domain.activity.model.valobj.OrderStateVO;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/7/8 09:25
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {
    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
