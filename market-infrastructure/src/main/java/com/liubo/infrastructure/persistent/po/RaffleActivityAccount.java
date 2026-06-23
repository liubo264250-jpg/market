package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 抽奖活动账户表
 * @TableName raffle_activity_account
 */
@TableName(value ="raffle_activity_account")
@Data
public class RaffleActivityAccount {
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
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 总次数
     */
    @TableField(value = "total_count")
    private Integer totalCount;

    /**
     * 总次数-剩余
     */
    @TableField(value = "total_count_surplus")
    private Integer totalCountSurplus;

    /**
     * 日次数
     */
    @TableField(value = "day_count")
    private Integer dayCount;

    /**
     * 日次数-剩余
     */
    @TableField(value = "day_count_surplus")
    private Integer dayCountSurplus;

    /**
     * 月次数
     */
    @TableField(value = "month_count")
    private Integer monthCount;

    /**
     * 月次数-剩余
     */
    @TableField(value = "month_count_surplus")
    private Integer monthCountSurplus;

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