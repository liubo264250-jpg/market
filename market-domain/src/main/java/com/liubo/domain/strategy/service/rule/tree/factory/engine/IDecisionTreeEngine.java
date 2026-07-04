package com.liubo.domain.strategy.service.rule.tree.factory.engine;

import com.liubo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * @author 68
 * 2026/6/20 21:56
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);
}
