package com.liubo.domain.award.service.distribute.impl;

import com.liubo.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.liubo.domain.award.model.entity.DistributeAwardEntity;
import com.liubo.domain.award.model.entity.UserAwardRecordEntity;
import com.liubo.domain.award.model.entity.UserCreditAwardEntity;
import com.liubo.domain.award.model.valobj.AwardStateVO;
import com.liubo.domain.award.repository.IAwardRepository;
import com.liubo.domain.award.service.distribute.IDistributeAward;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author 68
 * 2026/7/5 20:01
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {
    @Resource
    private IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        // 奖品ID
        Integer awardId = distributeAwardEntity.getAwardId();
        // 查询奖品ID 「优先走透传的随机积分奖品配置」
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = repository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("award_config 「" + awardConfig + "」配置不是一个范围值，如 1,100");
        }

        // 生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.completed
        );

        UserCreditAwardEntity userCreditAwardEntity = GiveOutPrizesAggregate.buildUserCreditAwardEntity(distributeAwardEntity.getUserId(), creditAmount);

        GiveOutPrizesAggregate giveOutPrizesAggregate = new GiveOutPrizesAggregate();
        giveOutPrizesAggregate.setUserId(distributeAwardEntity.getUserId());
        giveOutPrizesAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        giveOutPrizesAggregate.setUserCreditAwardEntity(userCreditAwardEntity);

        // 存储发奖对象
        repository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
