package com.liubo.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liubo.domain.credit.model.aggregate.TradeAggregate;
import com.liubo.domain.credit.model.entity.CreditAccountEntity;
import com.liubo.domain.credit.model.entity.CreditOrderEntity;
import com.liubo.domain.credit.model.valobj.AccountStatusVO;
import com.liubo.domain.credit.repository.ICreditRepository;
import com.liubo.infrastructure.persistent.dao.UserCreditAccountMapper;
import com.liubo.infrastructure.persistent.dao.UserCreditOrderMapper;
import com.liubo.infrastructure.persistent.po.UserCreditAccount;
import com.liubo.infrastructure.persistent.po.UserCreditOrder;
import com.liubo.infrastructure.persistent.redis.IRedisService;
import com.liubo.types.common.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 68
 * 2026/7/6 09:05
 */
@Repository
@Slf4j
public class CreditRepository implements ICreditRepository {

    @Resource
    private IRedisService redisService;

    @Resource
    private UserCreditAccountMapper userCreditAccountMapper;

    @Resource
    private UserCreditOrderMapper userCreditOrderMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        RLock lock = redisService.getLock(Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());
        // 积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        // 知识；仓储往上有业务语义，仓储往下到 dao 操作是没有业务语义的。所以不用在乎这块使用的字段名称，直接用持久化对象即可。
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());
        userCreditAccountReq.setAccountStatus(AccountStatusVO.OPEN.getCode());

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());
        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 编程式事务
            transactionTemplate.execute(status -> {
                try {
                    // 1. 保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountMapper.selectOne(Wrappers.<UserCreditAccount>lambdaQuery().eq(UserCreditAccount::getUserId, userId));
                    if (null == userCreditAccount) {
                        userCreditAccountMapper.insert(userCreditAccountReq);
                    } else {
                        userCreditAccountMapper.updateAddAmount(userCreditAccountReq);
                    }
                    // 2. 保存账户订单
                    userCreditOrderMapper.insert(userCreditOrderReq);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
            });
        } finally {
            if (lock.isHeldByCurrentThread()) {
                // 只有「当前线程确实持有这把锁」才去释放
                // 避免锁没拿到却去 unlock 的异常
                lock.unlock();
            }
        }
    }
}
