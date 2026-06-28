package com.liubo.domain.activity.service.armory;

import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 68
 * 2026/6/26 22:09
 */
@Service
public class ActivityArmoryDispatch implements IActivityArmory,IActivityDispatch{
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCount());
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }


    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySkuStockCount(cacheKey, stockCount);
    }

    @Override
    public boolean subtractionActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractionActivitySkuStock(sku, cacheKey, endDateTime);
    }
}
