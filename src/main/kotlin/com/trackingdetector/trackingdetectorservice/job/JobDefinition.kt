package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.util.HashUtils

data class JobDefinition(
    val jobName: String,
    val jobDescription: String,
    val cronExpression: String,
    ) {
    val jobId: String = HashUtils.sha256(this.jobName)
}
