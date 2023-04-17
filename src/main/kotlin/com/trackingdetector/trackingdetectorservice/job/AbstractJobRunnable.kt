package com.trackingdetector.trackingdetectorservice.job

abstract class AbstractJobRunnable(private val jobDefinition: JobDefinition) {
    var jobId: String = this.jobDefinition.jobId
    val jobName: String = this.jobDefinition.jobName
    val jobDescription: String = this.jobDefinition.jobDescription
    val cronExpression: String = this.jobDefinition.cronExpression

    abstract fun execute(jobPublisher: JobPublisher): Boolean
}
