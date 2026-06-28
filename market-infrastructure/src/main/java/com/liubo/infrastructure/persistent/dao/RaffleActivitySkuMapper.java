package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.RaffleActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【raffle_activity_sku】的数据库操作Mapper
* @createDate 2026-06-23 17:24:20
* @Entity com.liubo.infrastructure.persistent.po.RaffleActivitySku
*/
public interface RaffleActivitySkuMapper extends BaseMapper<RaffleActivitySku> {

    void updateActivitySkuStock(RaffleActivitySku raffleActivitySku);

    void clearActivitySkuStock(RaffleActivitySku raffleActivitySku);
}




