package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.TrainingFile
import org.springframework.data.mongodb.repository.MongoRepository

interface TrainingFileRepository : MongoRepository<TrainingFile, String>
