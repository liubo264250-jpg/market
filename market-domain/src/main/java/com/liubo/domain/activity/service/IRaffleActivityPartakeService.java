package com.liubo.domain.activity.service;

import com.liubo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.liubo.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @author 68
 * 2026/6/28 17:23
 */
public interface IRaffleActivityPartakeService {
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
