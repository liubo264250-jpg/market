package com.liubo.domain.award.service.distribute;

import com.liubo.domain.award.model.entity.DistributeAwardEntity;

/**
 * @author 68
 * 2026/7/5 20:00
 */
public interface IDistributeAward {
    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
