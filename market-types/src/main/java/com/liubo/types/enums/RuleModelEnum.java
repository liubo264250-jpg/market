package com.liubo.types.enums;

import com.liubo.types.exception.AppException;

/**
 * @author 68
 * 2026/6/2 09:45
 */
public enum RuleModelEnum {
    RULE_RANDOM("rule_random", "随机值计算"),
    RULE_LOCK("rule_lock", "抽奖几次后解锁"),
    RULE_LUCK_AWARD("rule_luck_award", "幸运奖(兜底奖品)"),
    RULE_WEIGHT("rule_weight", "权重规则"),
    RULE_BLACKLIST("rule_blacklist", "黑名单规则");

    private final String code;
    private final String desc;

    RuleModelEnum(String code, String desc) {
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


    public static RuleModelEnum getByCode(String code) {
        if (null == code) {
            return null;
        }
        for (RuleModelEnum anEnum : RuleModelEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        throw new AppException(ResponseCode.ENUM_NOT_FOUND.getCode(), ResponseCode.ENUM_NOT_FOUND.getInfo());
    }
}
