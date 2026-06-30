package com.liubo.domain.award.repository;

import com.liubo.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @author 68
 * 2026/6/28 22:39
 */
public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
