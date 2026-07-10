package com.liubo.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 68
 * 2026/6/23 17:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySkuEntity {
    /**
     * 商品sku - 把每一个组合当做一个商品
     */
    private Long sku;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动个人参与次数ID
     */
    private Long activityCountId;

    /**
     * 商品库存
     */
    private Integer stockCount;

    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;

    /**
     * 商品金额
     */
    private BigDecimal productAmount;
}
