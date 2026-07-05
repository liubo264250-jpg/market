package com.liubo.infrastructure.persistent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubo.infrastructure.persistent.po.RaffleActivityAccount;

/**
* @author liubo
* @description 针对表【raffle_activity_account(抽奖活动账户表)】的数据库操作Mapper
* @createDate 2026-06-23 17:25:35
* @Entity com.liubo.infrastructure.persistent.po.RaffleActivityAccount
*/
public interface RaffleActivityAccountMapper extends BaseMapper<RaffleActivityAccount> {

    int updateActivityAccountSubtractionQuota(RaffleActivityAccount raffleActivityAccount);

    void updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount updateActivityAccountDaySurplusImageQuota);

    void updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount updateActivityAccountMonthSurplusImageQuota);

    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);
}




