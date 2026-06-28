package com.liubo.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 68
 * 2026/6/28 17:45
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {
    create("create", "创建"),
    used("used", "已使用"),
    cancel("cancel", "已作废"),
    ;

    private final String code;
    private final String desc;

    public static UserRaffleOrderStateVO findByCode(String code) {
        for (UserRaffleOrderStateVO userRaffleOrderStateVO : values()) {
            if (userRaffleOrderStateVO.getCode().equals(code)) {
                return userRaffleOrderStateVO;
            }
        }
        return null;
    }
}
