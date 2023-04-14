package com.trackingdetector.trackingdetectorservice.converter

import com.trackingdetector.trackingdetectorservice.domain.KerasModel
import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import com.trackingdetector.trackingdetectorservice.representation.KerasModelRepresentation

object KerasModelToKerasModelRepresentationConverter {

    fun convert(model: KerasModel, trainingResults: List<TrainingResult>) : KerasModelRepresentation {
        val mappedTrainingResults = trainingResults
            .map(TrainingResultToTrainingResultRepresentationConverter::convert)
            .toList()
        return KerasModelRepresentation(model.id,
            model.modelName,
            model.modelDescription,
            model.modelJson,
            model.batchSize,
            model.epochs,
            model.trainingDataFilename,
            model.applicationName,
            model.modelStorageName,
            mappedTrainingResults)
    }
}