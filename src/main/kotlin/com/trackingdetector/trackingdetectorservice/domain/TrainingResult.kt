package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
data class TrainingResult(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val modelId: String,
    val accuracy: Double,
    val trainingRun: Instant
) {
    companion object {
        fun fromHashMap(modelId: String, trainingRun: Instant, items: HashMap<*, *>): TrainingResult {
            return TrainingResult(modelId = modelId, accuracy = items["accuracy"] as Double, trainingRun = trainingRun)
        }
    }
}
