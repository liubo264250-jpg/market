package com.liubo.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liubo.domain.strategy.service.armory.IStrategyArmory;
import com.liubo.domain.strategy.service.armory.IStrategyDispatch;
import com.liubo.infrastructure.persistent.dao.StrategyMapper;
import com.liubo.infrastructure.persistent.po.Strategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private StrategyMapper awardDao;

    @Resource
    private IStrategyArmory strategyService;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Test
    public void test_queryAwardList() {
        List<Strategy> list = awardDao.selectList(new LambdaQueryWrapper<>());
        System.out.println(list);
    }

    @Test
    public void test2() {
        strategyService.assembleLotteryStrategy(100001L);
    }

    @Test
    public void test4() {
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
    }

    @Test
    public void test5() {
        log.info("测试结果：{} - 4000 策略配置", strategyDispatch.getRandomAwardId(100001L, "4000:102,103,104,105"));
        log.info("测试结果：{} - 5000 策略配置", strategyDispatch.getRandomAwardId(100001L, "5000:102,103,104,105,106,107"));
        log.info("测试结果：{} - 6000 策略配置", strategyDispatch.getRandomAwardId(100001L, "6000:102,103,104,105,106,107,108,109"));
    }
}
