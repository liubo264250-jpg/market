package com.liubo.domain.activity.model.aggregate;

import com.liubo.domain.activity.model.entity.ActivityAccountDayEntity;
import com.liubo.domain.activity.model.entity.ActivityAccountEntity;
import com.liubo.domain.activity.model.entity.ActivityAccountMonthEntity;
import com.liubo.domain.activity.model.entity.UserRaffleOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/28 17:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartakeOrderAggregate {
    private ActivityAccountEntity activityAccountEntity;
    private String userId;
    private Long activityId;
    private ActivityAccountDayEntity activityAccountDayEntity;
    private boolean existAccountDay;
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    private boolean existAccountMonth;
    private UserRaffleOrderEntity userRaffleOrderEntity;
}
