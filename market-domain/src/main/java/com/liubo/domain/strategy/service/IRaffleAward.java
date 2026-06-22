package com.liubo.domain.strategy.service;

import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author 68
 * 2026/6/22 17:35
 */
public interface IRaffleAward {
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
