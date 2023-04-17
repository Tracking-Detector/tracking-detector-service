package com.trackingdetector.trackingdetectorservice.dto

data class KerasModelDto(
    val modelName: String,
    val modelDescription: String,
    val modelJson: String,
    val batchSize: Int,
    val epochs: Int,
    val trainingDataFilename: String,
    val applicationName: String,
    val modelStorageName: String
)
