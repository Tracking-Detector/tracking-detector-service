package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import javax.annotation.PostConstruct

abstract class JobConfiguration(private val jobMetaRepository: JobMetaRepository,
                                private val jobRunRepository: JobRunRepository,
                                private val taskScheduler: ThreadPoolTaskScheduler) {
    abstract val job: JobRunnable
    abstract val jobDefinition: JobDefinition


    @PostConstruct
    private fun registerJob() {
        taskScheduler.schedule(job::execute, CronTrigger(jobDefinition.cronExpression))
    }

}