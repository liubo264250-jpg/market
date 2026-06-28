package com.liubo.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/26 23:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ActivitySkuStockKeyVO {
    /**
     * 商品sku - 把每一个组合当做一个商品
     */
    private Long sku;

    /**
     * 活动ID
     */
    private Long activityId;
}
