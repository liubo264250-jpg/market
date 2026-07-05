package com.liubo.domain.strategy.model.valobj;

import lombok.*;

import java.util.List;

/**
 * @author 68
 * 2026/7/5 16:06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RuleWeightVO {
    // 原始规则值配置
    private String ruleValue;
    // 权重值
    private Integer weight;
    // 奖品配置
    private List<Integer> awardIds;
    // 奖品列表
    private List<Award> awardList;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Award {
        private Integer awardId;
        private String awardTitle;
    }
}
