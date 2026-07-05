package com.liubo.domain.rebate.model.entity;

import com.liubo.domain.rebate.event.SendRebateMessageEvent;
import com.liubo.domain.rebate.model.valobj.TaskStateVO;
import com.liubo.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/7/5 10:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    /**
     * 活动ID
     */
    private String userId;
    /**
     * 消息主题
     */
    private String topic;
    /**
     * 消息编号
     */
    private String messageId;
    /**
     * 消息主体
     */
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /**
     * 任务状态；create-创建、completed-完成、fail-失败
     */
    private TaskStateVO state;
}
