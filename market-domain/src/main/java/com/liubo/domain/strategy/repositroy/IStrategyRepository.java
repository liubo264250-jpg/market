package com.liubo.domain.strategy.repositroy;

import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;
import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.model.entity.StrategyRuleEntity;
import com.liubo.domain.strategy.model.valobj.RuleTreeVO;
import com.liubo.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.util.List;
import java.util.Map;

/**
 * @author 68
 * 2026/5/29 07:55
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String strategyId, int size, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable);

    Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategy(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String ruleModels);
}
