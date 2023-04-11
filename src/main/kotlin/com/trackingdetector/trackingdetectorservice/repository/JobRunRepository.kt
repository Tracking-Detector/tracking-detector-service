package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.JobRun
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface JobRunRepository : MongoRepository<JobRun, String> {

    fun findAllByJobId(jobId: String): List<JobRun>

    fun findAllByStartDateBefore(time: Instant): List<JobRun>

    fun findAllByJobIdOrderByEndDateAsc(jobId: String): List<JobRun>

}