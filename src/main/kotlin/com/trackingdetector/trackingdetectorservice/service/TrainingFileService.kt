package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.TrainingFile
import com.trackingdetector.trackingdetectorservice.repository.TrainingFileRepository
import org.springframework.stereotype.Service

@Service
class TrainingFileService(private val trainingFileRepository: TrainingFileRepository) {

    fun getAllTrainingFiles(): List<TrainingFile> {
        return this.trainingFileRepository.findAll()
    }
}
