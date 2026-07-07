package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户积分订单记录
 * @TableName user_credit_order
 */
@TableName(value ="user_credit_order")
@Data
public class UserCreditOrder {
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
     * 订单ID
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 交易名称
     */
    @TableField(value = "trade_name")
    private String tradeName;

    /**
     * 交易类型；forward-正向、reverse-逆向
     */
    @TableField(value = "trade_type")
    private String tradeType;

    /**
     * 交易金额
     */
    @TableField(value = "trade_amount")
    private BigDecimal tradeAmount;

    /**
     * 业务仿重ID - 外部透传。返利、行为等唯一标识
     */
    @TableField(value = "out_business_no")
    private String outBusinessNo;

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