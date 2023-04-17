package com.trackingdetector.trackingdetectorservice.representation

data class KerasModelRepresentation(
    val id: String,
    val modelName: String,
    val modelDescription: String,
    val modelJson: String,
    val batchSize: Int,
    val epochs: Int,
    val trainingDataFilename: String,
    val applicationName: String,
    val modelStorageName: String,
    val trainingResults: List<TrainingResultRepresentation>
)
