package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *
 * @TableName award
 */
@TableName(value = "award")
@Data
public class Award {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 抽奖奖品ID - 内部流转使用
     */
    @TableField(value = "award_id")
    private Integer awardId;
    /**
     * 奖品对接标识 - 每一个都是一个对应的发奖策略
     */
    @TableField(value = "award_key")
    private String awardKey;
    /**
     * 奖品配置信息
     */
    @TableField(value = "award_config")
    private String awardConfig;
    /**
     * 奖品内容描述
     */
    @TableField(value = "award_desc")
    private String awardDesc;
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
