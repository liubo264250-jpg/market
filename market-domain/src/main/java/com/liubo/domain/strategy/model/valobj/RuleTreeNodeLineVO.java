package com.liubo.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/20 21:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RuleTreeNodeLineVO {
    private String treeId;
    private String ruleNodeFrom;
    private String ruleNodeTo;
    private RuleLimitTypeVO ruleLimitType;
    private RuleLogicCheckTypeVO ruleLimitValue;
}
