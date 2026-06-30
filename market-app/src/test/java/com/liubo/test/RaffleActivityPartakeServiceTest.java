package com.liubo.test;

import com.alibaba.fastjson.JSON;
import com.liubo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.liubo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.liubo.domain.activity.service.IRaffleActivityPartakeService;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import com.liubo.domain.award.model.valobj.AwardStateVO;
import com.liubo.domain.award.service.IAwardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author 68
 * 2026/6/28 19:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RaffleActivityPartakeServiceTest {
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    private IAwardService awardService;


    @Test
    public void test_createOrder() {
        // 请求参数
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        // 调用接口
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrder));
    }

    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("xiaofuge");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardState(AwardStateVO.create);
            awardService.saveUserAwardRecord(userAwardRecordEntity);
            Thread.sleep(500);
        }
        new CountDownLatch(1).await();
    }
}
