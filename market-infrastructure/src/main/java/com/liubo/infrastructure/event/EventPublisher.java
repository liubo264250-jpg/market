package com.liubo.infrastructure.event;

import com.alibaba.fastjson.JSON;
import com.liubo.types.event.BaseEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/27 23:42
 */
@Slf4j
@Component
public class EventPublisher {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage) {
        try {
            String messageJson = JSON.toJSONString(eventMessage);
            rocketMQTemplate.convertAndSend(topic, messageJson);
            log.info("发送MQ消息 topic:{} message:{}", topic, messageJson);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(eventMessage), e);
            throw e;
        }
    }

    public void publish(String topic, String eventMessageJSON) {
        try {
            rocketMQTemplate.convertAndSend(topic, eventMessageJSON);
            log.info("发送MQ消息 topic:{} message:{}", topic, eventMessageJSON);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, eventMessageJSON, e);
            throw e;
        }
    }

}
