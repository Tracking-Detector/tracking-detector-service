package com.trackingdetector.trackingdetectorservice.job

abstract class JobRunnable(private val jobDefinition: JobDefinition) {
    val jobName: String = this.jobDefinition.jobName
    val jobDescription: String = this.jobDefinition.jobDescription
    val cronExpression: String = this.jobDefinition.cronExpression

    abstract fun execute(): Boolean


}