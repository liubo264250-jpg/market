package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.StrategyAward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【strategy_award】的数据库操作Mapper
* @createDate 2026-05-28 08:38:11
* @Entity com.liubo.infrastructure.persistent.po.StrategyAward
*/
public interface StrategyAwardMapper extends BaseMapper<StrategyAward> {

    void updateStrategyAwardStock(StrategyAward strategyAward);
}




