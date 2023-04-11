package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import java.time.Instant
import java.time.temporal.ChronoUnit

class CleanUpJob(jobDefinition: JobDefinition,
                 private val jobRunRepository: JobRunRepository) : JobRunnable(jobDefinition) {

    override fun execute(jobPublisher: JobPublisher): Boolean {
        jobPublisher.info("Start loading all JobDefinitions.")
        val deletionThreshHold = Instant.now().minus(30, ChronoUnit.DAYS)
        val jobRuns = this.jobRunRepository.findAllByStartDateBefore(deletionThreshHold)
        jobPublisher.info("Found ${jobRuns.size} JobRuns in the db.")
        if (jobRuns.isEmpty()) {
            jobPublisher.info("No jobRuns found before $deletionThreshHold. Skipped.")
            jobPublisher.skipped()
            return false
        }

        jobPublisher.info("Start deleting Jobruns after the $deletionThreshHold.")
        var delCount = 0
        for (jobRun in jobRuns) {
            this.jobRunRepository.deleteById(jobRun.id)
            delCount++
        }
        jobPublisher.info("Deleted $delCount Jobruns.")
        return true
    }
}