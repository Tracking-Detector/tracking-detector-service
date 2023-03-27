package com.trackingdetector.trackingdetectorservice.job

data class JobDefinition(
    val jobName: String,
    val jobDescription: String,
    val cronExpression: String,
    )
