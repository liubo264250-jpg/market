package com.liubo.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/7/7 09:15
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {
    OPEN("open", "可用"),
    CLOSE("close", "冻结"),
    ;

    private final String code;
    private final String info;
}
