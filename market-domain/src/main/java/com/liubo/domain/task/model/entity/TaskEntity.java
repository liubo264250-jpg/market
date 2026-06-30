package com.liubo.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/30 23:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息主体id
     */
    private String messageId;

    /**
     * 消息主体
     */
    private String message;

    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private String state;
}
