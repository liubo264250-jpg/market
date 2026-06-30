package com.liubo.domain.task.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/6/30 23:57
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {
    create("create", "创建"),
    completed("completed", "完成"),
    fail("fail", "失败"),
    ;

    private final String code;
    private final String desc;

}
