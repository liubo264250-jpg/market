package com.liubo.infrastructure.persistent.repository;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.award.model.valobj.TaskStateVO;
import com.liubo.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.liubo.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.liubo.domain.rebate.model.entity.TaskEntity;
import com.liubo.domain.rebate.model.valobj.BehaviorTypeVO;
import com.liubo.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.liubo.domain.rebate.repository.IBehaviorRebateRepository;
import com.liubo.infrastructure.event.EventPublisher;
import com.liubo.infrastructure.persistent.dao.DailyBehaviorRebateMapper;
import com.liubo.infrastructure.persistent.dao.TaskMapper;
import com.liubo.infrastructure.persistent.dao.UserBehaviorRebateOrderMapper;
import com.liubo.infrastructure.persistent.po.DailyBehaviorRebate;
import com.liubo.infrastructure.persistent.po.Task;
import com.liubo.infrastructure.persistent.po.UserBehaviorRebateOrder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/7/5 10:08
 */
@Repository
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private DailyBehaviorRebateMapper dailyBehaviorRebateMapper;

    @Resource
    private UserBehaviorRebateOrderMapper userBehaviorRebateOrderMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateMapper.selectList(Wrappers.<DailyBehaviorRebate>lambdaQuery()
                .eq(DailyBehaviorRebate::getBehaviorType, behaviorTypeVO.getCode()));
        return Optional.ofNullable(dailyBehaviorRebates).orElse(new ArrayList<>())
                .stream().map(rebate -> DailyBehaviorRebateVO.builder()
                        .behaviorType(rebate.getBehaviorType())
                        .rebateDesc(rebate.getRebateDesc())
                        .rebateType(rebate.getRebateType())
                        .rebateConfig(rebate.getRebateConfig())
                        .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
            // 用户行为返利订单对象
            UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
            userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
            userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
            userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
            userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
            userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
            userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
            userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
            userBehaviorRebateOrderMapper.insert(userBehaviorRebateOrder);

            // 任务对象
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setTopic(taskEntity.getTopic());
            task.setMessageId(taskEntity.getMessageId());
            task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
            task.setState(taskEntity.getState().getCode());
            taskMapper.insert(task);
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 同步发送MQ消息
                for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                    TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                    Task task = new Task();
                    task.setUserId(taskEntity.getUserId());
                    task.setMessageId(taskEntity.getMessageId());
                    try {
                        // 发送消息【在事务外执行，如果失败还有任务补偿】
                        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                        // 更新数据库记录，task 任务表
                        Task updateCompleted = new Task();
                        updateCompleted.setState(TaskStateVO.completed.getCode());
                        taskMapper.update(updateCompleted, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, task.getMessageId()));
                    } catch (Exception e) {
                        log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                        Task updateFail = new Task();
                        updateFail.setState(TaskStateVO.fail.getCode());
                        taskMapper.update(updateFail, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, task.getMessageId()));
                    }
                }
            }
        });
    }
}
