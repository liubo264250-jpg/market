package com.liubo.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rule_tree
 */
@TableName(value ="rule_tree")
@Data
public class RuleTree {
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
     * 规则树名称
     */
    @TableField(value = "tree_name")
    private String treeName;

    /**
     * 规则树描述
     */
    @TableField(value = "tree_desc")
    private String treeDesc;

    /**
     * 规则树根入口规则
     */
    @TableField(value = "tree_node_rule_key")
    private String treeNodeRuleKey;

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