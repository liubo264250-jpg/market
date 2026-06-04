package com.liubo.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    ENUM_NOT_FOUND("0004", "未找到枚举"),
    STRATEGY_RULE_WEIGHT_IS_NULL("0005", "未配置策略规则权重"),
    ;

    private String code;
    private String info;

}
