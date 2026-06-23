package com.liubo.domain.activity.service.rule;

import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author 68
 * 2026/6/23 22:19
 */
public interface IActionChain  extends IActionChainArmory{
    boolean action(ActivitySkuEntity activitySkuEntity,
                   ActivityEntity activityEntity,
                   ActivityCountEntity activityCountEntity);
}
