package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rule_tree_node_line
 */
@TableName(value ="rule_tree_node_line")
@Data
public class RuleTreeNodeLine {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 规则树ID
     */
    @TableField(value = "tree_id")
    private String treeId;

    /**
     * 规则Key节点 From
     */
    @TableField(value = "rule_node_from")
    private String ruleNodeFrom;

    /**
     * 规则Key节点 To
     */
    @TableField(value = "rule_node_to")
    private String ruleNodeTo;

    /**
     * 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围];
     */
    @TableField(value = "rule_limit_type")
    private String ruleLimitType;

    /**
     * 限定值（到下个节点）
     */
    @TableField(value = "rule_limit_value")
    private String ruleLimitValue;

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