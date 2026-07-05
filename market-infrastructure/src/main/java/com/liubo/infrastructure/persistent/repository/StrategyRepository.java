package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.strategy.model.entity.StrategyAwardEntity;
import com.liubo.domain.strategy.model.entity.StrategyEntity;
import com.liubo.domain.strategy.model.entity.StrategyRuleEntity;
import com.liubo.domain.strategy.model.valobj.*;
import com.liubo.domain.strategy.repository.IStrategyRepository;
import com.liubo.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.liubo.infrastructure.persistent.dao.*;
import com.liubo.infrastructure.persistent.po.*;
import com.liubo.infrastructure.persistent.redis.IRedisService;
import com.liubo.types.common.Constants;
import com.liubo.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.liubo.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;

/**
 * @author 68
 * 2026/5/29 08:06
 */
@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IRedisService redisService;

    @Resource
    private StrategyAwardMapper strategyAwardMapper;

    @Resource
    private StrategyMapper strategyMapper;

    @Resource
    private StrategyRuleMapper strategyRuleMapper;

    @Resource
    private RuleTreeMapper ruleTreeMapper;

    @Resource
    private RuleTreeNodeMapper ruleTreeNodeMapper;

    @Resource
    private RuleTreeNodeLineMapper ruleTreeNodeLineMapper;

    @Resource
    private RaffleActivityMapper raffleActivityMapper;
    @Autowired
    private RaffleActivityAccountMapper raffleActivityAccountMapper;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardEntityList(Long strategyId) {
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
    public int getRateRange(String key) {
        String cacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
        if (!redisService.isExists(cacheKey)) {
            throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        }
        return redisService.getValue(cacheKey);
    }

    @Override
    public StrategyEntity queryStrategyEntity(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) return strategyEntity;
        Strategy strategy = strategyMapper.selectOne(Wrappers.<Strategy>lambdaQuery().eq(Strategy::getStrategyId, strategyId));
        strategyEntity = Optional.ofNullable(strategy)
                .map(item -> StrategyEntity.builder()
                        .strategyId(strategy.getStrategyId())
                        .strategyDesc(strategy.getStrategyDesc())
                        .ruleModels(strategy.getRuleModels())
                        .build()).orElse(null);
        if (null == strategyEntity) return strategyEntity;
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel) {
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

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (null != ruleTreeVOCache) return ruleTreeVOCache;

        // 从数据库获取
        RuleTree ruleTree = ruleTreeMapper.selectOne(Wrappers.<RuleTree>lambdaQuery().eq(RuleTree::getTreeId, treeId));
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(Wrappers.<RuleTreeNode>lambdaQuery().eq(RuleTreeNode::getTreeId, treeId));
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineMapper.selectList(Wrappers.<RuleTreeNodeLine>lambdaQuery().eq(RuleTreeNodeLine::getTreeId, treeId));

        // 1. tree node line 转换Map结构
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = ruleTreeNodeLines.stream()
                .map(ruleTreeNodeLine -> RuleTreeNodeLineVO.builder()
                        .treeId(ruleTreeNodeLine.getTreeId())
                        .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                        .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                        .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                        .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                        .build())
                .collect(Collectors.groupingBy(RuleTreeNodeLineVO::getRuleNodeFrom));

        // 2. tree node 转换为Map结构
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeNodes.stream()
                .map(ruleTreeNode -> RuleTreeNodeVO.builder()
                        .treeId(ruleTreeNode.getTreeId())
                        .ruleKey(ruleTreeNode.getRuleKey())
                        .ruleDesc(ruleTreeNode.getRuleDesc())
                        .ruleValue(ruleTreeNode.getRuleValue())
                        .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                        .build())
                .collect(Collectors.toMap(RuleTreeNodeVO::getRuleKey, Function.identity(), (a, b) -> b));

        // 3. 构建 Rule Tree
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();
        redisService.setValue(cacheKey, ruleTreeVODB);
        return ruleTreeVODB;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, awardCount);
    }

    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> queue = redisService.getBlockingQueue(cacheKey);
        return queue.poll();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        strategyAwardMapper.updateStrategyAwardStock(strategyAward);
    }

    @Override
    public Boolean subtractionAwardStock(String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0) {
            // 库存小于0，恢复为0个
            redisService.setValue(cacheKey, 0);
            return false;
        }
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = false;
        if (null != endDateTime) {
            long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
            lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        } else {
            lock = redisService.setNx(lockKey);
        }
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = strategyAwardMapper.selectOne(Wrappers.<StrategyAward>lambdaQuery()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId));

        return StrategyAwardEntity.builder()
                .strategyId(strategyAward.getStrategyId())
                .awardId(strategyAward.getAwardId())
                .awardTitle(strategyAward.getAwardTitle())
                .awardSubtitle(strategyAward.getAwardSubtitle())
                .awardCount(strategyAward.getAwardCount())
                .awardCountSurplus(strategyAward.getAwardCountSurplus())
                .awardRate(strategyAward.getAwardRate())
                .ruleModels(strategyAward.getRuleModels())
                .sort(strategyAward.getSort())
                .build();
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        RaffleActivity raffleActivity = raffleActivityMapper.selectOne(Wrappers.<RaffleActivity>lambdaQuery().eq(RaffleActivity::getActivityId, activityId));
        return Optional.ofNullable(raffleActivity).map(RaffleActivity::getStrategyId).orElse(0L);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = queryStrategyIdByActivityId(activityId);
        List<StrategyAward> strategyAwardList = strategyAwardMapper.selectList(Wrappers.<StrategyAward>lambdaQuery().eq(StrategyAward::getStrategyId, strategyId));
        return Optional.ofNullable(strategyAwardList).orElse(new ArrayList<>())
                .stream()
                .map(award -> StrategyAwardEntity.builder()
                        .strategyId(award.getStrategyId())
                        .awardId(award.getAwardId())
                        .awardTitle(award.getAwardTitle())
                        .awardSubtitle(award.getAwardSubtitle())
                        .awardCount(award.getAwardCount())
                        .awardCountSurplus(award.getAwardCountSurplus())
                        .awardRate(award.getAwardRate())
                        .ruleModels(award.getRuleModels())
                        .sort(award.getSort())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if (null == treeIds || treeIds.length == 0) return null;
        List<String> treeIdList = Arrays.asList(treeIds);
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeMapper.selectList(Wrappers.<RuleTreeNode>lambdaQuery()
                .eq(RuleTreeNode::getRuleKey, RuleModelVO.RULE_LOCK.getCode())
                .in(RuleTreeNode::getTreeId, treeIdList));
        return ruleTreeNodeList.stream().collect(Collectors.toMap(
                RuleTreeNode::getTreeId,
                item -> Integer.valueOf(item.getRuleValue()),
                (existing, replacement) -> existing // 重复key保留旧值
        ));
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeight(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        List<RuleWeightVO> ruleWeightVOS = redisService.getValue(cacheKey);
        if (null != ruleWeightVOS) return ruleWeightVOS;

        ruleWeightVOS = new ArrayList<>();
        // 1. 查询权重规则配置
        StrategyRule strategyRule = strategyRuleMapper.selectOne(Wrappers.<StrategyRule>lambdaQuery()
                .eq(StrategyRule::getStrategyId, strategyId)
                .eq(StrategyRule::getRuleModel, DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode()));
        String ruleValue = Optional.ofNullable(strategyRule).map(StrategyRule::getRuleValue).orElse("");
        // 2. 借助实体对象转换规则
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        strategyRuleEntity.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        strategyRuleEntity.setRuleValue(ruleValue);
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        // 3. 遍历规则组装奖品配置
        Set<String> ruleWeightKeys = ruleWeightValues.keySet();
        for (String ruleWeightKey : ruleWeightKeys) {
            List<Integer> awardIds = ruleWeightValues.get(ruleWeightKey);
            List<RuleWeightVO.Award> awardList = new ArrayList<>();
            // 也可以修改为一次从数据库查询
            for (Integer awardId : awardIds) {
                StrategyAward strategyAwardReq = new StrategyAward();
                strategyAwardReq.setStrategyId(strategyId);
                strategyAwardReq.setAwardId(awardId);
                StrategyAward strategyAward = strategyAwardMapper.selectOne(Wrappers.<StrategyAward>lambdaQuery()
                        .eq(StrategyAward::getStrategyId, strategyId)
                        .eq(StrategyAward::getAwardId, awardId));
                awardList.add(RuleWeightVO.Award.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .build());
            }

            ruleWeightVOS.add(RuleWeightVO.builder()
                    .ruleValue(ruleValue)
                    .weight(Integer.valueOf(ruleWeightKey.split(Constants.COLON)[0]))
                    .awardIds(awardIds)
                    .awardList(awardList)
                    .build());
        }

        // 设置缓存 - 实际场景中，这类数据，可以在活动下架的时候统一清空缓存。
        redisService.setValue(cacheKey, ruleWeightVOS);

        return ruleWeightVOS;
    }

    @Override
    public Integer queryActivityAccountTotalUseCount(String userId, Long strategyId) {
        RaffleActivity raffleActivity = raffleActivityMapper.selectOne(Wrappers.<RaffleActivity>lambdaQuery().eq(RaffleActivity::getStrategyId, strategyId));
        Long activityId = Optional.ofNullable(raffleActivity).map(RaffleActivity::getActivityId).orElse(0L);
        RaffleActivityAccount raffleActivityAccount = raffleActivityAccountMapper.selectOne(Wrappers.<RaffleActivityAccount>lambdaQuery()
                .eq(RaffleActivityAccount::getUserId, userId)
                .eq(RaffleActivityAccount::getActivityId, activityId));
        Integer totalCount = Optional.ofNullable(raffleActivityAccount).map(RaffleActivityAccount::getTotalCount).orElse(0);
        Integer totalCountSurplus = Optional.ofNullable(raffleActivityAccount).map(RaffleActivityAccount::getTotalCountSurplus).orElse(0);

        // 返回计算使用量
        return totalCount - totalCountSurplus;
    }
}
