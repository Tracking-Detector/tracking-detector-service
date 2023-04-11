package com.trackingdetector.trackingdetectorservice.job

import com.nhaarman.mockitokotlin2.mock
import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class RequestDataExportJobTest {
    private lateinit var requestDataExportJob: RequestDataExportJob

    private val jobDefinition: JobDefinition = mock()

    private val minioService: MinioService = mock()
    private val featureExtractor: FeatureExtractor = mock()
    private val requestDataService: RequestDataService = mock()
    private val jobPublisher: JobPublisher = mock()
    @BeforeEach
    fun setUp() {
        Mockito.reset(jobDefinition,
            minioService, featureExtractor,
            requestDataService)
        requestDataExportJob = RequestDataExportJob(jobDefinition,
            minioService, featureExtractor,
            requestDataService)
    }

    @Test
    fun should_be_skipped_when_no_request_data_available() {
        // given

        // when
        this.requestDataExportJob.execute(jobPublisher)
        // then
    }
}