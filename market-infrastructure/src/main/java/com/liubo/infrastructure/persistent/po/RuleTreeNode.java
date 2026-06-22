package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *
 * @TableName rule_tree_node
 */
@TableName(value = "rule_tree_node")
@Data
public class RuleTreeNode {
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
     * 规则Key
     */
    @TableField(value = "rule_key")
    private String ruleKey;

    /**
     * 规则描述
     */
    @TableField(value = "rule_desc")
    private String ruleDesc;

    /**
     * 规则比值
     */
    @TableField(value = "rule_value")
    private String ruleValue;

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