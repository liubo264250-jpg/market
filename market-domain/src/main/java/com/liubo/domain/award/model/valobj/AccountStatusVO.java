package com.liubo.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/7/5 20:14
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {
    open("open", "可用"),
    close("close", "冻结"),
    ;

    private final String code;
    private final String desc;
}
