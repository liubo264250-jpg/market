package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName raffle_activity_sku
 */
@TableName(value ="raffle_activity_sku")
@Data
public class RaffleActivitySku {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品sku - 把每一个组合当做一个商品
     */
    @TableField(value = "sku")
    private Long sku;

    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private Long activityId;

    /**
     * 活动个人参与次数ID
     */
    @TableField(value = "activity_count_id")
    private Long activityCountId;

    /**
     * 商品库存
     */
    @TableField(value = "stock_count")
    private Integer stockCount;

    /**
     * 剩余库存
     */
    @TableField(value = "stock_count_surplus")
    private Integer stockCountSurplus;

    /**
     * 商品金额
     */
    @TableField(value = "product_amount")
    private BigDecimal productAmount;

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