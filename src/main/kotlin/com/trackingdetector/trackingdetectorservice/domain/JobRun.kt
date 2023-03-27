package com.trackingdetector.trackingdetectorservice.domain

import java.util.*

data class JobRun(
    val id: UUID = UUID.randomUUID(),
    val jobId: String,
    val status: String,
    val startDate: Date,
    val endDate: Date,
    val logs: String
)
