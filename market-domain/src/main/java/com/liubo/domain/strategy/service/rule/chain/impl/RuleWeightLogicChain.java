package com.liubo.domain.strategy.service.rule.chain.impl;

import com.liubo.domain.strategy.repositroy.IStrategyRepository;
import com.liubo.domain.strategy.service.armory.IStrategyDispatch;
import com.liubo.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.liubo.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 68
 * 2026/6/18 22:16
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    // 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
    public Long userScore = 0L;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("规则过滤-权重范围 userId:{} strategyId:{}", userId, strategyId);
        // 4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
        String strategyRuleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(strategyRuleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            return null;
        }
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Collections.reverseOrder())
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);
        if (null != nextValue) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }
        // 过滤其他责任链
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode();

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
