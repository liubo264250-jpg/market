package com.liubo.api;

import com.liubo.api.dto.ActivityDrawRequestDTO;
import com.liubo.api.dto.ActivityDrawResponseDTO;
import com.liubo.types.model.Response;

/**
 * @author 68
 * 2026/7/2 22:52
 */
public interface IRaffleActivityService {
    Response<Boolean> armory(Long activityId);

    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);
}
