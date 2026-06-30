package com.liubo.domain.task.repository;

import com.liubo.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @author 68
 * 2026/6/30 23:38
 */
public interface ITaskRepository {

    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
