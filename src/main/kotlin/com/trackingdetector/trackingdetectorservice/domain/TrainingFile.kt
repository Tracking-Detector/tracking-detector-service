package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TrainingFile(
    @Id
    val id: String,
    val fileName: String,
    val fileDescription: String
)
