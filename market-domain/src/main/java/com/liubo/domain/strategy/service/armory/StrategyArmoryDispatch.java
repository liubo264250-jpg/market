package com.liubo.domain.strategy.service.armory;

import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;
import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.model.entity.StrategyRuleEntity;
import com.liubo.domain.strategy.repository.IStrategyRepository;
import com.liubo.types.common.Constants;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.exception.AppException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author 68
 * 2026/5/29 07:48
 */
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntityList = repository.queryStrategyAwardEntityList(strategyId);
        //  缓存奖品库存【用于decr扣减库存使用】
        for (StrategyAwardEntity strategyAward : strategyAwardEntityList) {
            Integer awardId = strategyAward.getAwardId();
            Integer awardCount = strategyAward.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntityList);
        StrategyEntity strategy = repository.queryStrategyEntity(strategyId);
        if (null == strategy || StringUtils.isBlank(strategy.getRuleWeightRuleModel())) return true;
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRuleEntity(strategyId, strategy.getRuleWeightRuleModel());
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        for (Map.Entry<String, List<Integer>> entry : ruleWeightValues.entrySet()) {
            String key = entry.getKey();
            List<Integer> ruleWeightValueList = entry.getValue();
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntityList);
            strategyAwardEntitiesClone.removeIf(strategyAwardEntity -> !ruleWeightValueList.contains(strategyAwardEntity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(String.valueOf(strategyId));
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.subtractionAwardStock(cacheKey);
    }


    private void assembleLotteryStrategy(String strategyId, List<StrategyAwardEntity> strategyAwardEntityList) {
        if (CollectionUtils.isEmpty(strategyAwardEntityList)) return;
        // 获取总和概率
        BigDecimal totalAwardRate = strategyAwardEntityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取最小概率
        BigDecimal minAwardRate = strategyAwardEntityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        // 生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < awardRate.multiply(rateRange).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        // 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        // 转成map key=槽位 value=奖励id
        Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }
        repository.storeStrategyAwardSearchRateTable(strategyId, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
    }


    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(cacheKey, awardCount);
    }
}
