package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.domain.JobRun
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import java.time.Instant
import java.util.*

open class JobPublisher private constructor(
    private val jobId: String,
    private val jobMetaRepository: JobMetaRepository,
    private val jobRunRepository: JobRunRepository
) {
    companion object {
        fun build(
            jobId: String,
            jobMetaRepository: JobMetaRepository,
            jobRunRepository: JobRunRepository
        ): JobPublisher {
            return JobPublisher(jobId, jobMetaRepository, jobRunRepository)
        }
    }

    private var log = "\n"
    private var currentRunId: Optional<String> = Optional.empty()
    var isTerminated: Boolean = false

    open fun startJob() {
        createJobRun()
        updateLastJobRun()
    }

    open fun skipped() {
        this.isTerminated = true
        updateJobRun(Instant.now(), "SKIPPED")
    }

    open fun success() {
        this.isTerminated = true
        updateJobRun(Instant.now(), "SUCCESS")
    }

    open fun failure() {
        this.isTerminated = true
        updateJobRun(Instant.now(), "FAILURE")
    }

    open fun info(vararg message: String) {
        generateLog("INFO", message.toList())
    }

    open fun warn(vararg message: String) {
        generateLog("WARN", message.toList())
    }

    open fun error(vararg message: String) {
        generateLog("ERROR", message.toList())
    }

    private fun generateLog(level: String, messages: List<String>) {
        this.log += "${Instant.now()}-$level: ${messages.joinToString(" ")}\n"
        updateLog()
    }

    private fun createJobRun() {
        val jobRun = JobRun(
            jobId = this.jobId,
            startDate = Instant.now(),
            endDate = null,
            status = "RUNNING",
            logs = this.log
        )
        this.jobRunRepository.save(jobRun)
        this.currentRunId = Optional.of(jobRun.id)
    }

    private fun updateLog() {
        if (this.currentRunId.isEmpty) {
            throw Exception("Cannot update Log of Job which is not started")
        }
        val optSavedRun = this.jobRunRepository.findById(this.currentRunId.get())
        if (optSavedRun.isEmpty) {
            throw Exception("job run could not be found")
        }
        val savedRun = optSavedRun.get()
        val jobRun = JobRun(
            id = savedRun.id,
            jobId = savedRun.jobId,
            startDate = savedRun.startDate,
            endDate = savedRun.endDate,
            status = savedRun.status,
            logs = this.log
        )
        this.jobRunRepository.save(jobRun)
    }

    private fun updateJobRun(endTime: Instant, status: String) {
        if (this.currentRunId.isEmpty) {
            throw Exception("Cannot set job to '$status' when not started")
        }
        val savedRun = this.jobRunRepository.findById(this.currentRunId.get()).get()
        val jobRun = JobRun(
            id = savedRun.id,
            jobId = savedRun.jobId,
            startDate = savedRun.startDate,
            endDate = endTime,
            status = status,
            logs = this.log
        )
        this.jobRunRepository.save(jobRun)
    }

    private fun updateLastJobRun() {
        val jobMeta = this.jobMetaRepository.findById(this.jobId).get()
        val updated = JobMeta(
            id = jobMeta.id,
            jobName = jobMeta.jobName,
            jobDescription = jobMeta.jobDescription,
            lastJobRun = Instant.now(),
            cronPattern = jobMeta.cronPattern,
            enabled = jobMeta.enabled,
            latestJobRun = currentRunId.get()
        )
        this.jobMetaRepository.save(updated)
    }
}
