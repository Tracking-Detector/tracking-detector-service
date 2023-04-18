package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.TrainingFile
import com.trackingdetector.trackingdetectorservice.repository.TrainingFileRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class TrainingFileService(private val trainingFileRepository: TrainingFileRepository) {

    fun getAllTrainingFiles(): List<TrainingFile> {
        return this.trainingFileRepository.findAll()
    }

    fun getFileById(id: String): Optional<TrainingFile> {
        return this.trainingFileRepository.findById(id)
    }
}
