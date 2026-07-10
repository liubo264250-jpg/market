package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @author 68
 * 2026/7/10 09:51
 */
public interface IRaffleActivitySkuProductService {
    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);
}
