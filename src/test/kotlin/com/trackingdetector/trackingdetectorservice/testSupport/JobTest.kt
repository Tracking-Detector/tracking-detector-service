package com.trackingdetector.trackingdetectorservice.testSupport

import com.trackingdetector.trackingdetectorservice.controller.JobController
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import com.trackingdetector.trackingdetectorservice.util.HashUtils
import org.asynchttpclient.AsyncHttpClient
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant


@Component
class JobTest (
    @Value("\${application.path}")
    private val baseUrl: String,
    private val jobMetaRepository: JobMetaRepository,
    private val jobRunRepository: JobRunRepository,
    private val jobController: JobController,
    ) {
    var currentJobId : String? = null
    var jobStart: Instant? = null

    @Autowired
    lateinit var asyncHttpClient: AsyncHttpClient

    private fun startJob(jobName: String) {
        if (currentJobId != null) {
            throw Exception("Some Job is running already")
        }
        val jobId = HashUtils.sha256(jobName)
        currentJobId = jobId
        val result = jobController.triggerJob(jobId)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.statusCode)
    }

    fun startRequestDataExport204Job(): JobTest {
        startJob("RequestDataExport204Job")
        jobStart = Instant.now()
        return this
    }

    fun startCleanUpJob(): JobTest {
        startJob("JobRunCleanUpJob")
        return this
    }

    fun startModelTrainingJob(): JobTest {
        startJob("ModelTrainingJob")
        return this
    }

    fun and(): JobTest {
        return this
    }

    fun waitForJobCompletion(waitingThreshold: Long = 2000): JobTest {
        var jobMeta = jobMetaRepository.findById(currentJobId!!)
        while(jobMeta.isPresent && jobMeta.get().latestJobRun == null) {
            Thread.sleep(waitingThreshold)
            jobMeta = jobMetaRepository.findById(currentJobId!!)
        }
        var jobRun = jobRunRepository.findById(jobMeta.get().latestJobRun!!)
        while(jobRun.isPresent && jobRun.get().endDate == null) {
            Thread.sleep(waitingThreshold)
            jobRun = jobRunRepository.findById(jobMeta.get().latestJobRun!!)
        }
        return this
    }


    fun jobFinishedWithStatusOk() {
        val optJobMeta = jobMetaRepository.findById(currentJobId!!)
        if (optJobMeta.isEmpty) {
            throw Exception("Job could not be found")
        }
        val jobRun = jobRunRepository.findById(optJobMeta.get().latestJobRun!!)

        Assertions.assertEquals(jobRun.get().status, "SUCCESS")
        cleanUpRepository()
    }

    fun jobFinishedWithStatusFailure() {
        val optJobMeta = jobMetaRepository.findById(currentJobId!!)
        if (optJobMeta.isEmpty) {
            throw Exception("Job could not be found")
        }
        val jobMeta = optJobMeta.get()
        val jobRun = jobRunRepository.findById(optJobMeta.get().latestJobRun!!)

        Assertions.assertEquals(jobRun.get().status, "FAILURE")
        cleanUpRepository()
    }

    fun jobFinishedWithStatusSkipped() {
        val optJobMeta = jobMetaRepository.findById(currentJobId!!)
        if (optJobMeta.isEmpty) {
            throw Exception("Job could not be found")
        }
        val jobRun = jobRunRepository.findById(optJobMeta.get().latestJobRun!!)

        Assertions.assertEquals(jobRun.get().status, "SKIPPED")
        cleanUpRepository()
    }

    private fun cleanUpRepository() {
        val optJobMeta = jobMetaRepository.findById(currentJobId!!)
        if (optJobMeta.isEmpty) {
            throw Exception("Job could not be found")
        }
        val jobMeta = optJobMeta.get()
        jobRunRepository.deleteById(jobMeta.latestJobRun!!)
        jobMeta.latestJobRun = null
        jobMetaRepository.save(jobMeta)
        currentJobId = null
    }
}