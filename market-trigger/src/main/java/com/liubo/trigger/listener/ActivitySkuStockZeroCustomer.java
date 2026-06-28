package com.liubo.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liubo.domain.activity.service.ISkuStock;
import com.liubo.types.event.BaseEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/27 23:20
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.activity_sku_stock_zero}",          // 替换为具体 topic
        consumerGroup = "activity_sku_stock_zero_consumer_group" // 替换为具体消费组名
)
public class ActivitySkuStockZeroCustomer implements RocketMQListener<String> {
    @Resource
    private ISkuStock skuStock;

    @Override
    public void onMessage(String message) {
        log.info("收到MQ消息 topic:your_topic_name message:{}", message);
        try {
            // 反序列化，T 替换为具体业务类型
            BaseEvent.EventMessage<Long> eventMessage =
                    JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Long>>() {
                    });
            // 处理业务逻辑
            Long sku = eventMessage.getData();
            skuStock.clearActivitySkuStock(sku);
            skuStock.clearQueueValue();
        } catch (Exception e) {
            log.error("消费MQ消息异常 message:{}", message, e);
            // 抛出异常会触发重试，确认是否需要重试再决定是否 throw
            throw e;
        }
    }
}
