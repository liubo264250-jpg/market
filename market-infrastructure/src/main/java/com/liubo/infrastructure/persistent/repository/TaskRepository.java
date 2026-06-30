package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.task.model.entity.TaskEntity;
import com.liubo.domain.task.model.valobj.TaskStateVO;
import com.liubo.domain.task.repository.ITaskRepository;
import com.liubo.infrastructure.event.EventPublisher;
import com.liubo.infrastructure.persistent.dao.TaskMapper;
import com.liubo.infrastructure.persistent.po.Task;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/6/30 23:38
 */
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<Task> taskList = taskMapper.queryNoSendMessageTaskList();
        return Optional.ofNullable(taskList).orElse(new ArrayList<>()).stream().map(task -> TaskEntity.builder()
                .userId(task.getUserId())
                .topic(task.getTopic())
                .messageId(task.getMessageId())
                .message(task.getMessage())
                .state(task.getState())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task task = new Task();
        task.setState(TaskStateVO.completed.getCode());
        taskMapper.update(task, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, messageId));
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task task = new Task();
        task.setState(TaskStateVO.fail.getCode());
        taskMapper.update(task, Wrappers.<Task>lambdaUpdate().eq(Task::getUserId, userId).eq(Task::getMessageId, messageId));
    }
}
