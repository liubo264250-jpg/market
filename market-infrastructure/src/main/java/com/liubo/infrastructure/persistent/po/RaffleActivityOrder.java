package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抽奖活动单
 *
 * @TableName raffle_activity_order
 */
@TableName(value = "raffle_activity_order")
@Data
public class RaffleActivityOrder {
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
     * 商品sku
     */
    @TableField(value = "sku")
    private Long sku;

    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 活动名称
     */
    @TableField(value = "activity_name")
    private String activityName;

    /**
     * 抽奖策略ID
     */
    @TableField(value = "strategy_id")
    private Long strategyId;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 下单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    /**
     * 总次数
     */
    @TableField(value = "total_count")
    private Integer totalCount;

    /**
     * 日次数
     */
    @TableField(value = "day_count")
    private Integer dayCount;

    /**
     * 月次数
     */
    @TableField(value = "month_count")
    private Integer monthCount;

    /**
     * 订单状态（complete）
     */
    @TableField(value = "state")
    private String state;

    /**
     * 外部业务防重单号
     */
    @TableField(value = "out_business_no")
    private String outBusinessNo;

    /**
     * 支付金额【积分】
     */
    @TableField(value = "pay_amount")
    private BigDecimal payAmount;

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