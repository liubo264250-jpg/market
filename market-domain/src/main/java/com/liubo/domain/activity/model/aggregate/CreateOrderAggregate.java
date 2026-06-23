package com.liubo.domain.activity.model.aggregate;

import com.liubo.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/23 22:24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderAggregate {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 月次数
     */
    private Integer monthCount;

    private ActivityOrderEntity activityOrderEntity;

}
