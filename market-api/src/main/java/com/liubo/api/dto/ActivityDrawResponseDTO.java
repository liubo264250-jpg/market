package com.liubo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 68
 * 2026/7/2 22:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDrawResponseDTO implements Serializable {
    /**
     * 奖品ID
     */
    private Integer awardId;
    /**
     * 抽奖奖品标题
     */
    private String awardTitle;
    /**
     * 奖品顺序号
     */
    private Integer awardIndex;
}
