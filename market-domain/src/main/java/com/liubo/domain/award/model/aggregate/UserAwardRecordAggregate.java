package com.liubo.domain.award.model.aggregate;

import com.liubo.domain.award.model.entity.TaskEntity;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/28 22:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecordAggregate {
    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
