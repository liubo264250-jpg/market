package com.liubo.domain.strategy.service;

/**
 * @author 68
 * 2026/5/29 07:48
 */
public interface IStrategyArmory {
    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);
}
