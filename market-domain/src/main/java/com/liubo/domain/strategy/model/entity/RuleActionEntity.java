package com.liubo.domain.strategy.model.entity;

import com.liubo.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/8 09:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    public static class RaffleEntity {

    }

    // 抽奖之前
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖。
         */
        private String ruleWeightValueKey;

        /**
         * 奖品ID；
         */
        private Integer awardId;
    }

    public static class RaffleCenterEntity extends RaffleEntity {

    }


    public static class RaffleAfterEntity extends RaffleEntity {

    }

}
