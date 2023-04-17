package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RequestDataExport204JobConfiguration {

    val jobDefinition = JobDefinition(
        jobName = "RequestDataExport204Job",
        jobDescription = "Exports the requests data into a csv with a feature vector of size [204, 1].",
        cronExpression = "0 0 12 */7 * ?"
    )

    @Bean
    fun requestDataExport204JobRunnable(
        minioService: MinioService,
        featureExtractor204: FeatureExtractor,
        requestDataService: RequestDataService
    ): AbstractJobRunnable {
        return RequestDataExportAbstractJob(jobDefinition, minioService, featureExtractor204, requestDataService)
    }
}
