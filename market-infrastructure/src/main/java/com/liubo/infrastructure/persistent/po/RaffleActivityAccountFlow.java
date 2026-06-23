package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 抽奖活动账户流水表
 * @TableName raffle_activity_account_flow
 */
@TableName(value ="raffle_activity_account_flow")
@Data
public class RaffleActivityAccountFlow {
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
     * 流水ID - 生成的唯一ID
     */
    @TableField(value = "flow_id")
    private String flowId;

    /**
     * 流水渠道（activity-活动领取、sale-购买、redeem-兑换、free-免费赠送）
     */
    @TableField(value = "flow_channel")
    private String flowChannel;

    /**
     * 业务ID（外部透传，活动ID、订单ID）
     */
    @TableField(value = "biz_id")
    private String bizId;

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