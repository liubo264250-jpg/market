package com.liubo.domain.credit.service;

import com.liubo.domain.credit.model.entity.TradeEntity;

/**
 * @author 68
 * 2026/7/6 08:54
 */
public interface ICreditAdjustService {
    String createOrder(TradeEntity tradeEntity);
}
