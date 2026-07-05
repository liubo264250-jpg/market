package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.RaffleActivityAccountDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【raffle_activity_account_day(抽奖活动账户表-日次数)】的数据库操作Mapper
* @createDate 2026-06-28 12:38:23
* @Entity com.liubo.infrastructure.persistent.po.RaffleActivityAccountDay
*/
public interface RaffleActivityAccountDayMapper extends BaseMapper<RaffleActivityAccountDay> {

    int updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountDay updateRaffleActivityAccountDay);

    void addAccountQuota(RaffleActivityAccountDay raffleActivityAccountDay);
}




