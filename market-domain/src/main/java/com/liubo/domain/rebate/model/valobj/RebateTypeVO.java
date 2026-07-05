package com.liubo.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/7/5 10:50
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {
    SKU("sku", "活动库存充值商品"),
    INTEGRAL("integral", "用户活动积分"),
    ;

    private final String code;
    private final String info;
}
