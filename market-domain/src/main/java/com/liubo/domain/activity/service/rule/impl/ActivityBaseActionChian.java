package com.liubo.domain.activity.service.rule.impl;

import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.service.rule.AbstractActionChian;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/23 22:20
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChian extends AbstractActionChian {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
