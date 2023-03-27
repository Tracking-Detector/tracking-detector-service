package com.trackingdetector.trackingdetectorservice.domain

import java.util.*

data class JobMeta(
    val id: UUID = UUID.randomUUID(),
    val jobName: String,
    val jobDescription: String,
    val lastJobRun: Date,
    val cronPattern: String,
    val enabled: Boolean
)
