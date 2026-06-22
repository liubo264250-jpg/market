package com.liubo.domain.strategy.service.rule.chain;

import com.liubo.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author 68
 * 2026/6/18 21:28
 */
public interface ILogicChain extends ILogicChainArmory {

    DefaultChainFactory.StrategyAwardVO  logic(String userId, Long strategyId);

}
