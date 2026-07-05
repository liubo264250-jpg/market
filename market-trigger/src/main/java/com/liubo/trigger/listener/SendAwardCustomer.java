package com.liubo.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.liubo.domain.award.event.SendAwardMessageEvent;
import com.liubo.domain.award.model.entity.DistributeAwardEntity;
import com.liubo.domain.award.service.AwardService;
import com.liubo.types.event.BaseEvent;
import jakarta.annotation.Resource;
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

    @Resource
    private AwardService awardService;

    @Override
    public void onMessage(String message) {
        try {
            log.info("监听用户奖品发送消息  message: {}", message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.SendAwardMessage sendAwardMessage = eventMessage.getData();
            // 发放奖品
            DistributeAwardEntity distributeAwardEntity = new DistributeAwardEntity();
            distributeAwardEntity.setUserId(sendAwardMessage.getUserId());
            distributeAwardEntity.setOrderId(sendAwardMessage.getOrderId());
            distributeAwardEntity.setAwardId(sendAwardMessage.getAwardId());
            distributeAwardEntity.setAwardConfig(sendAwardMessage.getAwardConfig());
            awardService.distributeAward(distributeAwardEntity);
        } catch (Exception e) {
            log.error("监听用户奖品发送消息，消费失败 message: {}", message);
            throw e;
        }
    }
}
