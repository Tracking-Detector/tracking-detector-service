package com.trackingdetector.trackingdetectorservice.converter

import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import com.trackingdetector.trackingdetectorservice.representation.TrainingResultRepresentation

object TrainingResultToTrainingResultRepresentationConverter {

    fun convert(trainingResult: TrainingResult): TrainingResultRepresentation {
        return TrainingResultRepresentation(trainingResult.accuracy, trainingResult.trainingRun)
    }
}
