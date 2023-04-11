package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import com.trackingdetector.trackingdetectorservice.util.HashUtils
import javax.annotation.PostConstruct

abstract class JobRunnable(private val jobDefinition: JobDefinition) {
    var jobId: String = this.jobDefinition.jobId
    val jobName: String = this.jobDefinition.jobName
    val jobDescription: String = this.jobDefinition.jobDescription
    val cronExpression: String = this.jobDefinition.cronExpression


    abstract fun execute(jobPublisher: JobPublisher): Boolean


}