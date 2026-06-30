package com.liubo.domain.award.event;

import com.liubo.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 68
 * 2026/6/28 22:45
 */
@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendAwardMessage> {
    @Value("${rocketmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<SendAwardMessage> buildEventMessage(SendAwardMessage data) {
        return EventMessage.<SendAwardMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAwardMessage {
        /**
         * 用户id
         */
        private String userId;
        /**
         * 奖品ID
         */
        private Integer awardId;

        /**
         * 奖品标题（名称）
         */
        private String awardTitle;

    }
}
