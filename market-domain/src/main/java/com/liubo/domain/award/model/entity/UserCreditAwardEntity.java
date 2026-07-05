package com.liubo.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 68
 * 2026/7/5 20:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreditAwardEntity {
    /** 用户ID */
    private String userId;
    /** 积分值 */
    private BigDecimal creditAmount;
}
