package com.liubo.trigger.job;

import com.liubo.domain.task.model.entity.TaskEntity;
import com.liubo.domain.task.service.ITaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 68
 * 2026/6/28 22:00
 */
@Slf4j
@Component
public class SendMessageTaskJob {
    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor executor;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        List<TaskEntity> taskEntities = taskService.queryNoSendMessageTaskList();
        if (taskEntities.isEmpty()) return;
        for (TaskEntity taskEntity : taskEntities) {
            executor.execute(() -> {
                try {
                    taskService.sendMessage(taskEntity);
                    taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                } catch (Exception e) {
                    log.error("定时任务，发送MQ消息失败 userId: {} topic: {}", taskEntity.getUserId(), taskEntity.getTopic());
                    taskService.updateTaskSendMessageFail(taskEntity.getUserId(), taskEntity.getMessageId());
                }
            });
        }
    }
}
