package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.domain.JobRun
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import org.springframework.stereotype.Service

@Service
class JobService(
    private val jobMetaRepository: JobMetaRepository,
    private val jobRunRepository: JobRunRepository,
    private val jobScheduler: JobScheduler
) {

    fun getAllJobs(): List<JobMeta> {
        return this.jobMetaRepository.findAll()
    }

    fun getAllJobRunsForJob(jobId: String): List<JobRun> {
        return this.jobRunRepository.findAllByJobId(jobId)
    }

    fun getJobMetaById(jobId: String): JobMeta? {
        val job = this.jobMetaRepository.findById(jobId)
        if (job.isEmpty) {
            return null
        }
        return job.get()
    }

    fun triggerJobById(jobId: String): Boolean {
        return this.jobScheduler.triggerJobById(jobId)
    }

    fun jobIsActive(jobId: String): Boolean {
        val optJobMeta = this.jobMetaRepository.findById(jobId)
        if (optJobMeta.isEmpty) return false
        val jobMeta = optJobMeta.get()
        return true
    }

    fun toggleJobById(jobId: String) {
        val optionalJob = this.jobMetaRepository.findById(jobId)
        if (optionalJob.isEmpty) {
            return
        }
        val job = optionalJob.get()
        job.enabled = !job.enabled
        this.jobMetaRepository.save(job)
    }
}
