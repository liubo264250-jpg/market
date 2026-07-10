package com.liubo.domain.activity.service.quota;

import com.liubo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.liubo.domain.activity.model.entity.*;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.liubo.domain.activity.service.quota.policy.ITradePolicy;
import com.liubo.domain.activity.service.quota.rule.IActionChain;
import com.liubo.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author 68
 * 2026/6/28 15:48
 */
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {

    private final Map<String, ITradePolicy> tradePolicyGroup;


    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository,
                                              DefaultActivityChainFactory defaultActivityChainFactory,
                                              Map<String, ITradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyGroup = tradePolicyGroup;

    }

    @Override
    public UnpaidActivityOrderEntity createOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1. 参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 2. 查询未支付订单「一个月以内的未支付订单」
        UnpaidActivityOrderEntity unpaidCreditOrder =  activityRepository.queryUnpaidActivityOrder(skuRechargeEntity);
        if (null != unpaidCreditOrder) return unpaidCreditOrder;
        // 3. 查询基础信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        // 4. 活动动作规则校验
        IActionChain actionChain = defaultActivityChainFactory.openLogicChain();
        actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);
        // 5. 构建订单聚合对象
        CreateQuotaOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);
        // 6. 交易策略 - 【积分兑换，支付类订单】【返利无支付交易订单，直接充值到账】【订单状态变更交易类型策略】
        ITradePolicy tradePolicy = tradePolicyGroup.get(skuRechargeEntity.getOrderTradeType().getCode());
        tradePolicy.trade(createOrderAggregate);

        // 7. 返回单号
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        return UnpaidActivityOrderEntity.builder()
                .userId(userId)
                .orderId(activityOrderEntity.getOrderId())
                .outBusinessNo(activityOrderEntity.getOutBusinessNo())
                .payAmount(activityOrderEntity.getPayAmount())
                .build();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity,
                                                                     ActivitySkuEntity activitySkuEntity,
                                                                     ActivityEntity activityEntity,
                                                                     ActivityCountEntity activityCountEntity);
}
