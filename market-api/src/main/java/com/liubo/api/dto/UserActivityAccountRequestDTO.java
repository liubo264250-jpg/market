package com.liubo.api.dto;

import lombok.Data;

/**
 * @author 68
 * 2026/7/5 14:48
 */
@Data
public class UserActivityAccountRequestDTO {
    private String userId;
    private Long activityId;
}
