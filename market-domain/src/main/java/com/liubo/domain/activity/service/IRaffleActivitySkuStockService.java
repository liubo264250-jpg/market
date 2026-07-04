package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.ActivitySkuEntity;
import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * @author 68
 * 2026/6/28 15:57
 */
public interface IRaffleActivitySkuStockService {
    ActivitySkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    ActivitySkuEntity queryActivitySku(Long sku);
}
