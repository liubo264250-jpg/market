package com.liubo.infrastructure.persistent.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubo.infrastructure.persistent.dao.StrategyMapper;
import com.liubo.infrastructure.persistent.po.Strategy;
import com.liubo.infrastructure.persistent.repository.StrategyService;
import org.springframework.stereotype.Service;

/**
* @author liubo
* @description 针对表【strategy】的数据库操作Service实现
* @createDate 2026-05-27 23:33:43
*/
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy>
    implements StrategyService {

}




