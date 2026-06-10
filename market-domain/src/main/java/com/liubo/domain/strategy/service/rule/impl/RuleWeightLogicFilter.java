package com.liubo.domain.strategy.service.rule.impl;

import com.liubo.domain.strategy.model.entity.RuleActionEntity;
import com.liubo.domain.strategy.model.entity.RuleMatterEntity;
import com.liubo.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.liubo.domain.strategy.repositroy.IStrategyRepository;
import com.liubo.domain.strategy.service.annotation.LogicStrategy;
import com.liubo.domain.strategy.service.rule.ILogicFilter;
import com.liubo.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 68
 * 2026/6/8 09:26
 */
@Component
@Slf4j
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;
    public Long userScore = 4500L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        Long strategyId = ruleMatterEntity.getStrategyId();
        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), strategyId, ruleMatterEntity.getRuleModel());
        // 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
        String strategyRuleValue = repository.queryStrategyRuleValue(strategyId, ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(strategyRuleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        analyticalSortedKeys.sort(Collections.reverseOrder());
        Long nextValue = analyticalSortedKeys.stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);
        if (null != nextValue) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .ruleWeightValueKey(analyticalValueGroup.get(nextValue))
                            .strategyId(strategyId)
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    private Map<Long, String> getAnalyticalValue(String strategyRuleValue) {
        String[] ruleValueGroups = strategyRuleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
