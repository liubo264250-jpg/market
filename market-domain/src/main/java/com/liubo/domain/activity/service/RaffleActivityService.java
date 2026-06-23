package com.liubo.domain.activity.service;

import com.liubo.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/6/23 17:48
 */
@Service
public class RaffleActivityService extends  AbstractRaffleActivity{
    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
