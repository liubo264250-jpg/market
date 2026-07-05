package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户积分账户
 * @TableName user_credit_account
 */
@TableName(value ="user_credit_account")
@Data
public class UserCreditAccount {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 总积分，显示总账户值，记得一个人获得的总积分
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 可用积分，每次扣减的值
     */
    @TableField(value = "available_amount")
    private BigDecimal availableAmount;

    /**
     * 账户状态【open - 可用，close - 冻结】
     */
    @TableField(value = "account_status")
    private String accountStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}