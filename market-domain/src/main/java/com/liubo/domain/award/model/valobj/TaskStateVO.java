package com.liubo.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/6/28 22:33
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
