package com.trackingdetector.trackingdetectorservice.job

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class RequestDataExportJobTest {
    private lateinit var requestDataExportJob: RequestDataExportJob
    private val jobDefinition: JobDefinition = mock()
    private val minioService: MinioService = mock()
    private val featureExtractor: FeatureExtractor = mock()
    private val requestDataService: RequestDataService = mock()
    private val jobPublisher = mock<JobPublisher>()

    @BeforeEach
    fun setUp() {
        Mockito.reset(jobDefinition,
            minioService, featureExtractor,
            requestDataService, jobPublisher)
        requestDataExportJob = RequestDataExportJob(jobDefinition,
            minioService, featureExtractor,
            requestDataService)
    }

    @Test
    fun should_be_skipped_when_no_request_data_available() {
        // given
        whenever(requestDataService.getNumberOfRequestData()).thenReturn(0L)
        // when
        val result = this.requestDataExportJob.execute(jobPublisher)
        // then
        Assertions.assertEquals(false, result)
        verify(jobPublisher).skipped()
    }
}