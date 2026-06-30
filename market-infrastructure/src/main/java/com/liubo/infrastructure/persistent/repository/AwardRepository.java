package com.liubo.infrastructure.persistent.repository;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.liubo.domain.award.model.entity.TaskEntity;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import com.liubo.domain.award.model.valobj.TaskStateVO;
import com.liubo.domain.award.repository.IAwardRepository;
import com.liubo.infrastructure.event.EventPublisher;
import com.liubo.infrastructure.persistent.dao.TaskMapper;
import com.liubo.infrastructure.persistent.dao.UserAwardRecordMapper;
import com.liubo.infrastructure.persistent.po.Task;
import com.liubo.infrastructure.persistent.po.UserAwardRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author 68
 * 2026/6/28 23:09
 */
@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {

    @Resource
    private UserAwardRecordMapper userAwardRecordMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private EventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        userAwardRecordMapper.insert(userAwardRecord);
        taskMapper.insert(task);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    // 发送消息【在事务外执行，如果失败还有任务补偿】
                    eventPublisher.publish(task.getTopic(), task.getMessage());
                    // 更新数据库记录，task 任务表
                    Task updateCompleted = new Task();
                    updateCompleted.setState(TaskStateVO.completed.getCode());
                    taskMapper.update(updateCompleted, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, task.getMessageId()));
                } catch (Exception e) {
                    log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                    Task updateFail = new Task();
                    updateFail.setState(TaskStateVO.fail.getCode());
                    taskMapper.update(updateFail, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, task.getMessageId()));
                }
            }
        });
    }
}
