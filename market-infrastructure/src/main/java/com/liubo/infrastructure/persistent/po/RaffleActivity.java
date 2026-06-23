package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 抽奖活动表
 * @TableName raffle_activity
 */
@TableName(value ="raffle_activity")
@Data
public class RaffleActivity {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 活动描述
     */
    @TableField(value = "activity_desc")
    private String activityDesc;

    /**
     * 开始时间
     */
    @TableField(value = "begin_date_time")
    private Date beginDateTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_date_time")
    private Date endDateTime;

    /**
     * 抽奖策略ID
     */
    @TableField(value = "strategy_id")
    private Long strategyId;

    /**
     * 活动状态
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