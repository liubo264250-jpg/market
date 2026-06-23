package com.liubo.test;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.infrastructure.persistent.dao.AwardMapper;
import com.liubo.infrastructure.persistent.dao.RaffleActivityOrderMapper;
import com.liubo.infrastructure.persistent.po.Award;
import com.liubo.infrastructure.persistent.po.RaffleActivityOrder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author 68
 * 2026/6/23 15:28
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingTest {

    @Resource
    private AwardMapper awardMapper;

    @Resource
    private RaffleActivityOrderMapper raffleActivityOrderMapper;

    @Test
    public void test1() {
        List<Award> awards = awardMapper.selectList(Wrappers.<Award>lambdaQuery().eq(Award::getId,1));
        System.out.println(awards);
    }

    @Test
    public void test2() {
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId("lsadjaldas87");
        raffleActivityOrder.setActivityId(112L);
        raffleActivityOrder.setActivityName("aa");
        raffleActivityOrder.setStrategyId(22L);
        raffleActivityOrder.setOrderId("22222");
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setState("not_used");
        raffleActivityOrderMapper.insert(raffleActivityOrder);
    }

    @Test
    public void test3() {
        RaffleActivityOrder raffleActivityOrder = raffleActivityOrderMapper.selectOne(Wrappers.<RaffleActivityOrder>lambdaQuery()
                .eq(RaffleActivityOrder::getUserId, "lsadjaldas87"));
        System.out.println(raffleActivityOrder);
    }

}
