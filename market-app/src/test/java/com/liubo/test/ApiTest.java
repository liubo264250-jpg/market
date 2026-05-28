package com.liubo.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Test
    public void test_queryAwardList() {
        List<Strategy> list = awardDao.selectList(new LambdaQueryWrapper<>());
        System.out.println(list);
    }

}
