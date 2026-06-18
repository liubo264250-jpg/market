package com.liubo.domain.strategy.service.rule.chain;

/**
 * @author 68
 * 2026/6/18 21:28
 */
public interface ILogicChainArmory {
    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}
