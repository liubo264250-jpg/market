package com.liubo.api.dto;

import lombok.Data;

/**
 * @author 68
 * 2026/7/10 09:32
 */
@Data
public class SkuProductShopCartRequestDTO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * sku 商品
     */
    private Long sku;
}
