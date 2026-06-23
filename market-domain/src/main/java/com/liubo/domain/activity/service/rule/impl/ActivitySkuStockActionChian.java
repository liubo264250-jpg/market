package com.liubo.domain.activity.service.rule.impl;

import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.service.rule.AbstractActionChian;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/23 22:21
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChian extends AbstractActionChian {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        return true;
    }
}
