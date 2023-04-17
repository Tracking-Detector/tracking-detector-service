package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.KerasModel
import org.springframework.data.mongodb.repository.MongoRepository

interface KerasModelRepository : MongoRepository<KerasModel, String>
