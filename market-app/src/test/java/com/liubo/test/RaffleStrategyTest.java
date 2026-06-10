package com.liubo.test;

import com.alibaba.fastjson.JSON;
import com.liubo.domain.strategy.model.entity.RaffleAwardEntity;
import com.liubo.domain.strategy.model.entity.RaffleFactorEntity;
import com.liubo.domain.strategy.service.IRaffleStrategy;
import com.liubo.domain.strategy.service.IStrategyArmory;
import com.liubo.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 68
 * 2026/6/10 09:46
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IStrategyArmory strategyArmory;

    @Before
    public void setUp() {
//        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 40500L);
        strategyArmory.assembleLotteryStrategy(100001L);
    }

    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("xiaofuge")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }
}
