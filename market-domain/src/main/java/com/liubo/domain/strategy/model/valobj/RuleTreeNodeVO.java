package com.liubo.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 68
 * 2026/6/20 21:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RuleTreeNodeVO {
    private String treeId;
    private String ruleKey;
    private String ruleDesc;
    private String ruleValue;
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
