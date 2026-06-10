package com.liubo.domain.strategy.service.rule;

import com.liubo.domain.strategy.model.entity.RuleActionEntity;
import com.liubo.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author 68
 * 2026/6/8 09:22
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
