package com.liubo.trigger.job;

import com.liubo.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.liubo.domain.activity.service.IRaffleActivitySkuStockService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 68
 * 2026/6/26 23:38
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {
    @Resource
    private IRaffleActivitySkuStockService raffleActivitySkuStockService;

    @Scheduled(cron = "0/1 * * * * ?")
    public void exec() {
        try {
//            log.info("定时任务，更新活动商品库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = raffleActivitySkuStockService.takeQueueValue();
            if (null == activitySkuStockKeyVO) return;
            log.info("定时任务，更新活动商品库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            raffleActivitySkuStockService.updateActivitySkuStock(activitySkuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，更新活动商品库存失败", e);
        }
    }
}
