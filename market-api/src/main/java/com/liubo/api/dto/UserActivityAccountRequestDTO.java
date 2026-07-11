package com.liubo.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 68
 * 2026/7/5 14:48
 */
@Data
public class UserActivityAccountRequestDTO implements Serializable {
    private String userId;
    private Long activityId;
}
