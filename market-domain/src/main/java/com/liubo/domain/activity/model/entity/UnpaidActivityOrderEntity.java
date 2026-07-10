package com.liubo.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 68
 * 2026/7/10 09:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidActivityOrderEntity {
    // 用户ID
    private String userId;
    // 订单ID
    private String orderId;
    // 外部透传ID
    private String outBusinessNo;
    // 订单金额
    private BigDecimal payAmount;
}
