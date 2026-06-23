package com.liubo.domain.activity.service.rule;

/**
 * @author 68
 * 2026/6/23 22:19
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
