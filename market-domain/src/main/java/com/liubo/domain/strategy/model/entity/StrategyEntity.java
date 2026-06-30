package com.liubo.domain.strategy.model.entity;

import com.liubo.types.common.Constants;
import com.liubo.domain.strategy.model.valobj.RuleModelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author 68
 * 2026/5/29 07:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {
    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    /**
     * 规则模型，rule配置的模型同步到此表，便于使用
     */
    private String ruleModels;

    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeightRuleModel() {
        return Arrays.stream(Optional.ofNullable(this.ruleModels())
                .orElse(new String[0]))
                .filter(RuleModelVO::hitRuleWeight)
                .findFirst()
                .orElse(null);
    }
}
