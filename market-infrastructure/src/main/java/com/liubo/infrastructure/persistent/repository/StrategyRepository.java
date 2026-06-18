package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;
import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.model.entity.StrategyRuleEntity;
import com.liubo.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.liubo.domain.strategy.repositroy.IStrategyRepository;
import com.liubo.infrastructure.persistent.dao.StrategyAwardMapper;
import com.liubo.infrastructure.persistent.dao.StrategyMapper;
import com.liubo.infrastructure.persistent.dao.StrategyRuleMapper;
import com.liubo.infrastructure.persistent.po.Strategy;
import com.liubo.infrastructure.persistent.po.StrategyAward;
import com.liubo.infrastructure.persistent.po.StrategyRule;
import com.liubo.infrastructure.persistent.redis.IRedisService;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/5/29 08:06
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IRedisService redisService;

    @Resource
    private StrategyAwardMapper strategyAwardMapper;

    @Resource
    private StrategyMapper strategyMapper;

    @Resource
    private StrategyRuleMapper strategyRuleMapper;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String strategyAwardRedisKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntityList = redisService.getValue(strategyAwardRedisKey);
        if (CollectionUtils.isNotEmpty(strategyAwardEntityList)) return strategyAwardEntityList;
        List<StrategyAward> strategyAwardList = strategyAwardMapper.selectList(Wrappers.<StrategyAward>lambdaQuery()
                .eq(StrategyAward::getStrategyId, strategyId));

        strategyAwardEntityList = strategyAwardList.stream()
                .map(strategyAward -> StrategyAwardEntity.builder()
                        .strategyId(strategyAward.getStrategyId())
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardSubtitle(strategyAward.getAwardSubtitle())
                        .awardCount(strategyAward.getAwardCount())
                        .awardCountSurplus(strategyAward.getAwardCountSurplus())
                        .awardRate(strategyAward.getAwardRate())
                        .ruleModels(strategyAward.getRuleModels())
                        .sort(strategyAward.getSort())
                        .build())
                .collect(Collectors.toList());
        redisService.setValue(strategyAwardRedisKey, strategyAwardEntityList);
        return strategyAwardEntityList;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable) {
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(shuffleStrategyAwardSearchRateTable);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public int getRateRange(String strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public StrategyEntity queryStrategy(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) return strategyEntity;
        Strategy strategy = strategyMapper.selectOne(Wrappers.<Strategy>lambdaQuery().eq(Strategy::getStrategyId, strategyId));
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleMapper.selectOne(Wrappers.<StrategyRule>lambdaQuery()
                .eq(StrategyRule::getStrategyId, strategyId)
                .eq(StrategyRule::getRuleModel, ruleModel));
        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleValue(strategyRuleRes.getRuleValue())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = strategyRuleMapper.selectOne(Wrappers.<StrategyRule>lambdaQuery()
                .eq(StrategyRule::getStrategyId, strategyId)
                .eq(awardId != null, StrategyRule::getAwardId, awardId)
                .eq(StrategyRule::getRuleModel, ruleModel));
        return Optional.ofNullable(strategyRule).map(StrategyRule::getRuleValue).orElse("");
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        StrategyRule strategyRule = strategyRuleMapper.selectOne(Wrappers.<StrategyRule>lambdaQuery()
                .eq(StrategyRule::getStrategyId, strategyId)
                .eq(StrategyRule::getRuleModel, ruleModel));
        return Optional.ofNullable(strategyRule).map(StrategyRule::getRuleValue).orElse("");
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = strategyAwardMapper.selectOne(Wrappers.<StrategyAward>lambdaQuery()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId));
        String ruleModels = Optional.ofNullable(strategyAward).map(StrategyAward::getRuleModels).orElse("");
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }
}
