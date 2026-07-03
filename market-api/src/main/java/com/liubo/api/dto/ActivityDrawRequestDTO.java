package com.liubo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/7/2 22:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawRequestDTO {
    private String userId;
    private String activityId;
}
