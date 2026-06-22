package com.liubo.domain.strategy.service.rule.chain.factory;

import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.repositroy.IStrategyRepository;
import com.liubo.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 68
 * 2026/6/18 23:45
 */
@Service
public class DefaultChainFactory {
    private final Map<String, ILogicChain> logicChainGroup;
    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategy(strategyId);
        String[] ruleModels = strategy.ruleModels();
        if (null == ruleModels || 0 == ruleModels.length) return logicChainGroup.get(LogicModel.RULE_DEFAULT.code);
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain currentChain = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            currentChain = currentChain.appendNext(nextChain);
        }
        currentChain.appendNext(logicChainGroup.get(LogicModel.RULE_DEFAULT.code));
        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /**
         * 抽奖奖品ID - 内部流转使用
         */
        private Integer awardId;
        /**
         *
         */
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }

}
