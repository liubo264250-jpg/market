package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.RaffleActivityOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【raffle_activity_order_000(抽奖活动单)】的数据库操作Mapper
* @createDate 2026-06-23 17:25:35
* @Entity com.liubo.infrastructure.persistent.po.RaffleActivityOrder000
*/
public interface RaffleActivityOrderMapper extends BaseMapper<RaffleActivityOrder> {

    RaffleActivityOrder queryUnpaidActivityOrder(RaffleActivityOrder raffleActivityOrderReq);
}




