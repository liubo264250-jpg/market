package com.liubo.domain.award.service;

import com.liubo.domain.award.event.SendAwardMessageEvent;
import com.liubo.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.liubo.domain.award.model.entity.DistributeAwardEntity;
import com.liubo.domain.award.model.entity.TaskEntity;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import com.liubo.domain.award.model.valobj.TaskStateVO;
import com.liubo.domain.award.repository.IAwardRepository;
import com.liubo.domain.award.service.distribute.IDistributeAward;
import com.liubo.types.event.BaseEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 68
 * 2026/6/28 21:59
 */
@Service
@Slf4j
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    private final Map<String, IDistributeAward> distributeAwardMap;

    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDistributeAward> distributeAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

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

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        // 奖品Key
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品，奖品ID不存在。awardKey:{}", awardKey);
            return;
        }
        // 奖品服务
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (null == distributeAward) {
            log.error("分发奖品，对应的服务不存在。awardKey:{}", awardKey);
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }
        // 发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }
}
