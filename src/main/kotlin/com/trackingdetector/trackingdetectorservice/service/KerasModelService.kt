package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.KerasModel
import com.trackingdetector.trackingdetectorservice.dto.KerasModelDto
import com.trackingdetector.trackingdetectorservice.repository.KerasModelRepository
import com.trackingdetector.trackingdetectorservice.util.HashUtils
import org.springframework.stereotype.Service

@Service
class KerasModelService(private val kerasModelRepository: KerasModelRepository) {

    fun getAllKerasModels(): List<KerasModel> {
        return this.kerasModelRepository.findAll()
    }

    fun createKerasModel(kerasModelDto: KerasModelDto) {
        val id = HashUtils.sha256(kerasModelDto.modelName)
        val existingModel = this.kerasModelRepository.findById(id)
        if (existingModel.isPresent) {
            throw Exception()
        }
        val kerasModel = KerasModel(
            id,
            kerasModelDto.modelName,
            kerasModelDto.modelDescription,
            kerasModelDto.modelJson,
            kerasModelDto.batchSize,
            kerasModelDto.epochs,
            kerasModelDto.trainingDataFilename,
            kerasModelDto.applicationName,
            kerasModelDto.modelStorageName
        )
        this.kerasModelRepository.save(kerasModel)
    }

    fun getKerasModelById(modelId: String) : KerasModel {
        val model  = this.kerasModelRepository.findById(modelId)
        if (model.isEmpty) {
            throw Exception("Keras model for given id: $modelId could not be found")
        }
        return model.get()
    }

}