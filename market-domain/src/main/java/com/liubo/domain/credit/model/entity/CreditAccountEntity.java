package com.liubo.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 68
 * 2026/7/6 09:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAccountEntity {
    /** 用户ID */
    private String userId;
    /** 可用积分，每次扣减的值 */
    private BigDecimal adjustAmount;
}
