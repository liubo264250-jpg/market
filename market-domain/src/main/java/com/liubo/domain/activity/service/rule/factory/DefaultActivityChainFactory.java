package com.liubo.domain.activity.service.rule.factory;

import com.liubo.domain.activity.service.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 68
 * 2026/6/23 22:18
 */
@Service
public class DefaultActivityChainFactory {
    private final Map<String, IActionChain> actionChainMap;

    public DefaultActivityChainFactory(Map<String, IActionChain> actionChainMap) {
        this.actionChainMap = actionChainMap;
    }

    public IActionChain openLogicChain() {
        IActionChain actionChain = actionChainMap.get(actionModel.ACTIVITY_BASE_ACTION.getCode());
        actionChain.appendNext(actionChainMap.get(actionModel.ACTIVITY_SKU_STOCK_ACTION.getCode()));
        return actionChain;
    }

    @Getter
    @AllArgsConstructor
    public enum actionModel {

        ACTIVITY_BASE_ACTION("activity_base_action", "活动的库存、时间校验"),
        ACTIVITY_SKU_STOCK_ACTION("activity_sku_stock_action", "活动sku库存"),
        ;

        private final String code;
        private final String info;
    }
}
