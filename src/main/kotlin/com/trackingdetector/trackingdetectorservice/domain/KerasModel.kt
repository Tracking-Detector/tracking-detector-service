package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class KerasModel(
    @Id
    val id: String,
    val modelName: String,
    val modelDescription: String,
    val modelJson: String,
    val batchSize: Int,
    val epochs: Int,
    val trainingDataFilename: String,
    val applicationName: String,
    val modelStorageName: String,
    )