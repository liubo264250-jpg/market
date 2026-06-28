package com.liubo.domain.activity.repository;

import com.liubo.domain.activity.model.aggregate.CreateOrderAggregate;
import com.liubo.domain.activity.model.entity.ActivityCountEntity;
import com.liubo.domain.activity.model.entity.ActivityEntity;
import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;

/**
 * @author 68
 * 2026/6/23 17:33
 */
public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO);

    ActivitySkuStockKeyVO takeQueueValue();

    void updateActivitySkuStock(Long sku);

    boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void clearActivitySkuStock(Long sku);

    void clearQueueValue();
}
