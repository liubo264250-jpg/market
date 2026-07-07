package com.liubo.domain.credit.repository;

import com.liubo.domain.credit.model.aggregate.TradeAggregate;

/**
 * @author 68
 * 2026/7/6 08:51
 */
public interface ICreditRepository {
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
