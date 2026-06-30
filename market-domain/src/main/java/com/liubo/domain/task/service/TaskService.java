package com.liubo.domain.task.service;

import com.liubo.domain.task.model.entity.TaskEntity;
import com.liubo.domain.task.repository.ITaskRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 68
 * 2026/6/28 21:59
 */
@Service
public class TaskService implements ITaskService {
    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId,messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFail(userId,messageId);
    }
}
