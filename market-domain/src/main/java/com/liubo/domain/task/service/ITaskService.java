package com.liubo.domain.task.service;

import com.liubo.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author 68
 * 2026/6/28 21:59
 */
public interface ITaskService {
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
