package com.liubo.api;

import com.liubo.api.dto.*;
import com.liubo.types.model.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 68
 * 2026/7/2 22:52
 */
public interface IRaffleActivityService {
    Response<Boolean> armory(Long activityId);

    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    Response<Boolean> calendarSignRebate(String userId);

    Response<Boolean> isCalendarSignRebate(String userId);

    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

    Response<Boolean> creditPayExchangeSku(SkuProductShopCartRequestDTO request);

    Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId);

    Response<BigDecimal> queryUserCreditAccount(String userId);
}
