package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
data class JobRun(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val jobId: String,
    val status: String,
    val startDate: Instant,
    val endDate: Instant?,
    val logs: String
)
