package com.liubo.domain.strategy.service.rule.tree;

import com.liubo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author 68
 * 2026/6/20 21:55
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);
}
