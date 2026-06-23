package com.liubo.domain.activity.service.rule;

/**
 * @author 68
 * 2026/6/23 22:20
 */
public abstract class AbstractActionChian implements IActionChain {
    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
