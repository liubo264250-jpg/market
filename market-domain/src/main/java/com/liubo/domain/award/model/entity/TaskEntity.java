package com.liubo.domain.award.model.entity;

import com.liubo.domain.award.event.SendAwardMessageEvent;
import com.liubo.domain.award.model.valobj.TaskStateVO;
import com.liubo.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/6/28 22:31
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
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;

    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private TaskStateVO state;
}
