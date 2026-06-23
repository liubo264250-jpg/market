package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 抽奖活动次数配置表
 * @TableName raffle_activity_count
 */
@TableName(value ="raffle_activity_count")
@Data
public class RaffleActivityCount {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动次数编号
     */
    @TableField(value = "activity_count_id")
    private Long activityCountId;

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