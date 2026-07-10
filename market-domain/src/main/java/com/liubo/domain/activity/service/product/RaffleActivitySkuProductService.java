package com.liubo.domain.activity.service.product;

import com.liubo.domain.activity.model.entity.SkuProductEntity;
import com.liubo.domain.activity.repository.IActivityRepository;
import com.liubo.domain.activity.service.IRaffleActivitySkuProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 68
 * 2026/7/10 09:54
 */
@Service
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {

    @Resource
    private IActivityRepository repository;


    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return repository.querySkuProductEntityListByActivityId(activityId);
    }
}
