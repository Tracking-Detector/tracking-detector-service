package com.trackingdetector.trackingdetectorservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class SchedulerConfiguration : SchedulingConfigurer {
    companion object {
        private const val POOL_SIZE = 10
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val threadPoolTaskScheduler = ThreadPoolTaskScheduler()
        threadPoolTaskScheduler.poolSize = POOL_SIZE;
        threadPoolTaskScheduler.setThreadNamePrefix("scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}