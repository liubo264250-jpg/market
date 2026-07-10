package com.liubo.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/7/8 09:44
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {
    credit_pay_trade("credit_pay_trade", "创建"),
    rebate_no_pay_trade("rebate_no_pay_trade", "创建"),
    ;

    private final String code;
    private final String desc;
}
