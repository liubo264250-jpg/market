package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户行为返利流水订单表
 * @TableName user_behavior_rebate_order
 */
@TableName(value ="user_behavior_rebate_order")
@Data
public class UserBehaviorRebateOrder {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 行为类型（sign 签到、openai_pay 支付）
     */
    @TableField(value = "behavior_type")
    private String behaviorType;

    /**
     * 返利描述
     */
    @TableField(value = "rebate_desc")
    private String rebateDesc;

    /**
     * 返利类型（sku 活动库存充值商品、integral 用户活动积分）
     */
    @TableField(value = "rebate_type")
    private String rebateType;

    /**
     * 返利配置【sku值，积分值】
     */
    @TableField(value = "rebate_config")
    private String rebateConfig;

    /**
     * 业务ID - 拼接的唯一值
     */
    @TableField(value = "biz_id")
    private String bizId;

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