package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.job.AbstractJobRunnable
import com.trackingdetector.trackingdetectorservice.job.JobPublisher
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class JobScheduler(
    private val abstractJobRunnable: List<AbstractJobRunnable>,
    private val taskScheduler: ThreadPoolTaskScheduler,
    private val jobMetaRepository: JobMetaRepository,
    private val jobRunRepository: JobRunRepository
) {

    @PostConstruct
    private fun initScheduler() {
        for (runnable in abstractJobRunnable) {
            registerJob(runnable)
            taskScheduler.schedule({ start(runnable) }, CronTrigger(runnable.cronExpression))
        }
    }

    private fun start(runnable: AbstractJobRunnable) {
        val jobMeta = this.jobMetaRepository.findById(runnable.jobId).get()
        if (!jobMeta.enabled) {
            return
        }
        if (jobMeta.latestJobRun != null) {
            val optRun = jobRunRepository.findById(jobMeta.latestJobRun!!)
            if (optRun.isEmpty) {
                return
            }
            val run = optRun.get()
            if (run.status == "RUNNING") {
                return
            }
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

    private fun registerJob(runnable: AbstractJobRunnable) {
        val optionalJobMeta = this.jobMetaRepository.findById(runnable.jobId)
        if (optionalJobMeta.isEmpty) {
            val registerJobMeta = JobMeta(
                id = runnable.jobId,
                jobName = runnable.jobName,
                jobDescription = runnable.jobDescription,
                cronPattern = runnable.cronExpression,
                lastJobRun = null,
                enabled = true,
                latestJobRun = null
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

    private fun getRunnable(jobId: String): AbstractJobRunnable {
        val runnable = this.abstractJobRunnable.find {
            it.jobId == jobId
        } ?: throw Exception("Runnable for $jobId could not be found.")
        return runnable
    }
}
