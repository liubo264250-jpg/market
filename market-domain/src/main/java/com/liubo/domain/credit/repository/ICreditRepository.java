package com.liubo.domain.credit.repository;

import com.liubo.domain.credit.model.aggregate.TradeAggregate;
import com.liubo.domain.credit.model.entity.CreditAccountEntity;

/**
 * @author 68
 * 2026/7/6 08:51
 */
public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);

    CreditAccountEntity queryUserCreditAccount(String userId);
}
