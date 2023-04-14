package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import com.trackingdetector.trackingdetectorservice.repository.TrainingResultRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TrainingResultService(private val trainingResultRepository: TrainingResultRepository) {


    fun createTrainingResult(trainingResult: TrainingResult) {
        this.trainingResultRepository.save(trainingResult)
    }

    fun getAllTrainingResultsForModel(modelId: String): List<TrainingResult> {
        return this.trainingResultRepository.findAllByModelId(modelId)
    }

}