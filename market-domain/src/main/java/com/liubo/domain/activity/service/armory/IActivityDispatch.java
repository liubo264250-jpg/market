package com.liubo.domain.activity.service.armory;

import java.util.Date;

/**
 * @author 68
 * 2026/6/26 22:09
 */
public interface IActivityDispatch {
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
