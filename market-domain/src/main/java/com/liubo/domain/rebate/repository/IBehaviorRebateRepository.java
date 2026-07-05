package com.liubo.domain.rebate.repository;

import com.liubo.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.liubo.domain.rebate.model.valobj.BehaviorTypeVO;
import com.liubo.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @author 68
 * 2026/7/5 10:01
 */
public interface IBehaviorRebateRepository {
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
