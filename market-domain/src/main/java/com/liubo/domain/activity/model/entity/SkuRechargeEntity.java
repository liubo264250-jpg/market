package com.liubo.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/23 22:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuRechargeEntity {
    /**
     * 商品sku - 把每一个组合当做一个商品
     */
    private Long sku;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 外部业务防重编号
     */
    private String outBusinessNo;
}
