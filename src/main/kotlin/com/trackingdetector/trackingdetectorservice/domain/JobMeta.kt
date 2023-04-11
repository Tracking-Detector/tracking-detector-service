package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
data class JobMeta(
    @Id
    val id: String,
    var jobName: String,
    var jobDescription: String,
    var lastJobRun: Instant?,
    var cronPattern: String,
    var enabled: Boolean,
    var latestJobRun: String?
)
