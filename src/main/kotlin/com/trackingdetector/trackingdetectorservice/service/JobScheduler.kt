package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.job.JobPublisher
import com.trackingdetector.trackingdetectorservice.job.JobRunnable
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class JobScheduler(private val jobRunnable: List<JobRunnable>,
                   private val taskScheduler: ThreadPoolTaskScheduler,
                   private val jobMetaRepository: JobMetaRepository,
                   private val jobRunRepository: JobRunRepository) {


    @PostConstruct
    private fun initScheduler() {
        for (runnable in jobRunnable) {
            registerJob(runnable)
            taskScheduler.schedule({ start(runnable) }, CronTrigger(runnable.cronExpression))
        }
    }

    private fun start(runnable: JobRunnable) {
        val jobMeta = this.jobMetaRepository.findById(runnable.jobId).get()
        if (!jobMeta.enabled || jobMeta.latestJobRun != null) {
            return
        }
        val publisher = JobPublisher.build(runnable.jobId, jobMetaRepository, jobRunRepository)
        try {
            publisher.startJob()
            val result: Boolean = runnable.execute(publisher)
            if (publisher.isTerminated) {
                return
            }
            if (result) {
                publisher.success()
            } else {
                publisher.failure()
            }
        } catch (e: Exception) {
            publisher.error("An error occurred at: ", e.stackTraceToString())
            publisher.failure()
        }
    }

    private fun registerJob(runnable: JobRunnable) {
        val optionalJobMeta = this.jobMetaRepository.findById(runnable.jobId)
        if (optionalJobMeta.isEmpty) {
            val registerJobMeta = JobMeta(
                id = runnable.jobId,
                jobName = runnable.jobName,
                jobDescription = runnable.jobDescription,
                cronPattern = runnable.cronExpression,
                lastJobRun = null,
                enabled = true,
                latestJobRun = null,
            )
            this.jobMetaRepository.save(registerJobMeta)
        } else {
            val jobMeta = optionalJobMeta.get()
            jobMeta.jobName = runnable.jobName
            jobMeta.jobDescription = runnable.jobDescription
            jobMeta.cronPattern = runnable.cronExpression
            this.jobMetaRepository.save(jobMeta)
        }
    }

    fun triggerJobById(jobId: String): Boolean {
        return try {
            taskScheduler.execute {
                start(getRunnable(jobId))
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getRunnable(jobId: String) : JobRunnable {
        val runnable = this.jobRunnable.find {
            it.jobId == jobId
        } ?: throw Exception("Runnable for $jobId could not be found.")
        return runnable
    }
}