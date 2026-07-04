package com.liubo.domain.strategy.service;

import java.util.Map;

/**
 * @author 68
 * 2026/7/3 23:09
 */
public interface IRaffleRule {
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
