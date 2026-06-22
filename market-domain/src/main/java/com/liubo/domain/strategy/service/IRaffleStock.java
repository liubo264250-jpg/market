package com.liubo.domain.strategy.service;

import com.liubo.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author 68
 * 2026/6/22 12:09
 */
public interface IRaffleStock {

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
