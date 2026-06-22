package com.liubo.domain.strategy.repositroy;

import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;
import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.model.entity.StrategyRuleEntity;
import com.liubo.domain.strategy.model.valobj.RuleTreeVO;
import com.liubo.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.liubo.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.List;
import java.util.Map;

/**
 * @author 68
 * 2026/5/29 07:55
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String strategyId, int size, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable);

    Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategyEntity(Long strategyId);

    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String ruleModels);

    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    Boolean subtractionAwardStock(String cacheKey);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);
}
