package com.trackingdetector.trackingdetectorservice.repository

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import org.springframework.data.repository.CrudRepository

interface JobMetaRepository : CrudRepository<JobMeta, String> {
}