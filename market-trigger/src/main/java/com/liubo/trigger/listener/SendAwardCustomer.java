package com.liubo.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/28 22:00
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.send_award}",          // 替换为具体 topic
        consumerGroup = "send_award_consumer_group" // 替换为具体消费组名
)
public class SendAwardCustomer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        try {
            log.info("监听用户奖品发送消息 message: {}", message);
        } catch (Exception e) {
            log.error("监听用户奖品发送消息，消费失败 message: {}", message);
            throw e;
        }
    }
}
