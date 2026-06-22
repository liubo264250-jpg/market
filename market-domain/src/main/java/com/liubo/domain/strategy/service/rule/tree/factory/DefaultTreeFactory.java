package com.liubo.domain.strategy.service.rule.tree.factory;

import com.liubo.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.liubo.domain.strategy.model.valobj.RuleTreeVO;
import com.liubo.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.liubo.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.liubo.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 68
 * 2026/6/20 21:56
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class TreeActionEntity<T> {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class StrategyAwardVO {
        private Integer awardId;
        private String awardRuleValue;
    }
}
