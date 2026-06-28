package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户中奖记录表
 * @TableName user_award_record
 */
@TableName(value ="user_award_record")
@Data
public class UserAwardRecord {
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
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 抽奖策略ID
     */
    @TableField(value = "strategy_id")
    private Long strategyId;

    /**
     * 抽奖订单ID【作为幂等使用】
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 奖品ID
     */
    @TableField(value = "award_id")
    private Integer awardId;

    /**
     * 奖品标题（名称）
     */
    @TableField(value = "award_title")
    private String awardTitle;

    /**
     * 中奖时间
     */
    @TableField(value = "award_time")
    private Date awardTime;

    /**
     * 奖品状态；create-创建、completed-发奖完成
     */
    @TableField(value = "award_state")
    private String awardState;

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