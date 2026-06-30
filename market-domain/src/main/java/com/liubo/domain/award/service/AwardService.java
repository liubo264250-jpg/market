package com.liubo.domain.award.service;

import com.liubo.domain.award.event.SendAwardMessageEvent;
import com.liubo.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.liubo.domain.award.model.entity.TaskEntity;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import com.liubo.domain.award.model.valobj.TaskStateVO;
import com.liubo.domain.award.repository.IAwardRepository;
import com.liubo.types.event.BaseEvent;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/6/28 21:59
 */
@Service
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage =
                sendAwardMessageEvent.buildEventMessage(sendAwardMessage);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);

        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .taskEntity(taskEntity)
                .userAwardRecordEntity(userAwardRecordEntity)
                .build();

        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }
}
