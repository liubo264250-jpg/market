package com.liubo.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.liubo.domain.activity.model.entity.SkuRechargeEntity;
import com.liubo.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.liubo.domain.rebate.event.SendRebateMessageEvent;
import com.liubo.domain.rebate.model.valobj.RebateTypeVO;
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
 * 2026/7/5 10:43
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.send_rebate}",          // 替换为具体 topic
        consumerGroup = "send_rebate_consumer_group" // 替换为具体消费组名
)
public class RebateMessageCustomer implements RocketMQListener<String> {

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @Override
    public void onMessage(String message) {
        try {
            if (message.equals("abc")) return;
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();
            if (!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())) {
                log.info("监听用户行为返利消息 - 非sku奖励暂时不处理 message: {}", message);
                return;
            }
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听用户行为返利消息，消费重复 message: {}", message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听用户行为返利消息，消费失败 message: {}", message, e);
            throw e;
        }
    }
}
