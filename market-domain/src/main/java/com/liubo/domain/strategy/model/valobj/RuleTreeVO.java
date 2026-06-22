package com.liubo.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 68
 * 2026/6/20 21:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RuleTreeVO {
    private String treeId;
    private String treeName;
    private String treeDesc;
    private String treeRootRuleNode;
    private Map<String, RuleTreeNodeVO> treeNodeMap;
}
