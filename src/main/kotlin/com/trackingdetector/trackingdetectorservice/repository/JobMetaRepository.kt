package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface JobMetaRepository : MongoRepository<JobMeta, String> {

    fun findByJobName(jobName: String) : JobMeta?


}