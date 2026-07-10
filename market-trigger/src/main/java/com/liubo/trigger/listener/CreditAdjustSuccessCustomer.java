package com.liubo.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liubo.domain.activity.model.entity.DeliveryOrderEntity;
import com.liubo.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.liubo.domain.credit.envent.CreditAdjustSuccessMessageEvent;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.event.BaseEvent;
import com.liubo.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/7/9 23:40
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.credit_adjust_success}",          // 替换为具体 topic
        consumerGroup = "credit_adjust_success_consumer_group" // 替换为具体消费组名
)
public class CreditAdjustSuccessCustomer implements RocketMQListener<String> {
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @Override
    public void onMessage(String message) {
        try {
            log.info("监听积分账户调整成功消息，进行交易商品发货  message: {}", message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();

            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
            deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());
            raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 message: {}", message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听积分账户调整成功消息，进行交易商品发货失败 message: {}", message, e);
            throw e;
        }
    }
}
