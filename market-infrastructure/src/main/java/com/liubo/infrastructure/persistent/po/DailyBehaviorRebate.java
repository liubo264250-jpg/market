package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 日常行为返利活动配置
 * @TableName daily_behavior_rebate
 */
@TableName(value ="daily_behavior_rebate")
@Data
public class DailyBehaviorRebate {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 返利配置
     */
    @TableField(value = "rebate_config")
    private String rebateConfig;

    /**
     * 状态（open 开启、close 关闭）
     */
    @TableField(value = "state")
    private String state;

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