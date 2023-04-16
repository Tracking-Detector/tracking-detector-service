package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.util.HashUtils

open class JobDefinition(
    var jobName: String,
    var jobDescription: String,
    var cronExpression: String,
    ) {
    var jobId: String = HashUtils.sha256(this.jobName)
}
