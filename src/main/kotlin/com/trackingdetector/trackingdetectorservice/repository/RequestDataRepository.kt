package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.RequestData
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface RequestDataRepository : MongoRepository<RequestData, String> {
    fun streamAllBy(): Stream<RequestData>
}