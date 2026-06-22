package com.liubo.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/22 14:21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StrategyAwardStockKeyVO {
    private Long strategyId;
    private Integer awardId;
}
