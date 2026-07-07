package com.liubo.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/7/6 09:04
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {
    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;

    private final String name;

}
