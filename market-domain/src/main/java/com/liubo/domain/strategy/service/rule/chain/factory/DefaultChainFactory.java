package com.liubo.domain.strategy.service.rule.chain.factory;

import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.repositroy.IStrategyRepository;
import com.liubo.domain.strategy.service.rule.chain.ILogicChain;
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
        if (null == ruleModels || 0 == ruleModels.length) return logicChainGroup.get("default");
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain currentChain = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            currentChain = currentChain.appendNext(nextChain);
        }
        currentChain.appendNext(logicChainGroup.get("default"));
        return logicChain;
    }
}
