package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;

/**
 * @author 68
 * 2026/6/26 22:59
 */
public interface ISkuStock {
    ActivitySkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

}
