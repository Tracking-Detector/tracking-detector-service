package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CleanUpJobConfiguration  {

    val jobDefinition = JobDefinition(
        jobName = "JobRunCleanUpJob",
        jobDescription = "Deletes old Job runs from the mongodb",
        cronExpression = "0 0 12 */7 * ?"
    )
    
    @Bean
    fun cleanUpJobRunnable(jobRunRepository: JobRunRepository): JobRunnable {
        return CleanUpJob(jobDefinition, jobRunRepository)
    }

}