package com.trackingdetector.trackingdetectorservice.acceptanceTest

import com.trackingdetector.trackingdetectorservice.domain.JobRun
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import com.trackingdetector.trackingdetectorservice.testSupport.AbstractSpringTest
import com.trackingdetector.trackingdetectorservice.testSupport.JobTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.time.temporal.ChronoUnit


class CleanUpJobAcceptanceTest : AbstractSpringTest() {

    @Autowired
    private lateinit var jobTest: JobTest

    @Autowired
    private lateinit var jobRunRepository: JobRunRepository

    @BeforeEach
    fun setUp() {
        jobRunRepository.deleteAll()
    }

    @Test
    fun should_delete_old_job_runs() {
        // given
        jobRunRepository.save(JobRun(
            id = "someId",
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(60, ChronoUnit.DAYS),
            endDate= Instant.now().minus(59, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        jobRunRepository.save(JobRun(
            id = "someId2",
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(40, ChronoUnit.DAYS),
            endDate= Instant.now().minus(39, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        jobRunRepository.save(JobRun(
            id = "someId3",
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(20, ChronoUnit.DAYS),
            endDate= Instant.now().minus(19, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        // when
        jobTest.startCleanUpJob()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusOk()
        // then
        assertEquals(1L, jobRunRepository.count())
    }

    @Test
    fun should_be_skipped_when_no_old_job_runs_in_repo() {
        // given
        jobRunRepository.save(JobRun(
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(10, ChronoUnit.DAYS),
            endDate= Instant.now().minus(9, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        jobRunRepository.save(JobRun(
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(15, ChronoUnit.DAYS),
            endDate= Instant.now().minus(14, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        jobRunRepository.save(JobRun(
            jobId= "someOldJobId",
            status= "SUCCESS",
            startDate= Instant.now().minus(20, ChronoUnit.DAYS),
            endDate= Instant.now().minus(19, ChronoUnit.DAYS),
            logs= "Info:"
        ))
        // when
        jobTest.startCleanUpJob()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusSkipped()
        // then
        assertEquals(3L, jobRunRepository.count())
    }

}