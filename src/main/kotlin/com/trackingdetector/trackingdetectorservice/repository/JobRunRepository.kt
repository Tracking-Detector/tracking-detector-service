package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.JobRun
import org.springframework.data.repository.CrudRepository

interface JobRunRepository : CrudRepository<JobRun, String> {
}