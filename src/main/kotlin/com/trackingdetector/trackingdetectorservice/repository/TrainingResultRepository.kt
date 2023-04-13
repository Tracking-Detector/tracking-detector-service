package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import org.springframework.data.mongodb.repository.MongoRepository

interface TrainingResultRepository : MongoRepository<TrainingResult, String> {
    fun findAllByModelId(modelId: String): List<TrainingResult>

}