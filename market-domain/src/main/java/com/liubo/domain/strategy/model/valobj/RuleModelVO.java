package com.liubo.domain.strategy.model.valobj;

/**
 * @author 68
 * 2026/6/2 09:45
 */
public enum RuleModelVO {
    RULE_RANDOM("rule_random", "随机值计算"),
    RULE_LOCK("rule_lock", "抽奖几次后解锁"),
    RULE_LUCK_AWARD("rule_luck_award", "幸运奖(兜底奖品)"),
    RULE_WEIGHT("rule_weight", "权重规则"),
    RULE_BLACKLIST("rule_blacklist", "黑名单规则");

    private final String code;
    private final String desc;

    RuleModelVO(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static boolean hitRuleWeight(String ruleModel) {
        return RULE_WEIGHT.getCode().equals(ruleModel);
    }
}
