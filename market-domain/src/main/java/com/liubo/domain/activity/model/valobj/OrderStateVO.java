package com.liubo.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/6/23 22:59
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {
    completed("completed", "完成");

    private final String code;
    private final String desc;
}
