package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.RequestData
import org.springframework.data.repository.CrudRepository

interface RequestDataRepository : CrudRepository<RequestData, String> {
}