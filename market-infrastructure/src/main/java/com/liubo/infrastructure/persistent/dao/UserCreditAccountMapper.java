package com.liubo.infrastructure.persistent.dao;

import com.liubo.infrastructure.persistent.po.UserCreditAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author liubo
* @description 针对表【user_credit_account(用户积分账户)】的数据库操作Mapper
* @createDate 2026-07-05 19:57:36
* @Entity com.liubo.infrastructure.persistent.po.UserCreditAccount
*/
public interface UserCreditAccountMapper extends BaseMapper<UserCreditAccount> {

    int updateAddAmount(UserCreditAccount userCreditAccountReq);
}




