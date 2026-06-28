package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.RaffleActivityAccountMonth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【raffle_activity_account_month(抽奖活动账户表-月次数)】的数据库操作Mapper
* @createDate 2026-06-28 12:38:23
* @Entity com.liubo.infrastructure.persistent.po.RaffleActivityAccountMonth
*/
public interface RaffleActivityAccountMonthMapper extends BaseMapper<RaffleActivityAccountMonth> {

    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountMonth updateRaffleActivityAccountMonth);
}




