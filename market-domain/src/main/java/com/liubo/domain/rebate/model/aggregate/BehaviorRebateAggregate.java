package com.liubo.domain.rebate.model.aggregate;

import com.liubo.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.liubo.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/7/5 09:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateAggregate {
    /**
     * 用户ID
     */
    private String userId;
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    private TaskEntity taskEntity;
}
