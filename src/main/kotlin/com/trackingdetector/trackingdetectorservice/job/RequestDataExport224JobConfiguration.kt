package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.domain.TrainingFile
import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.repository.TrainingFileRepository
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import com.trackingdetector.trackingdetectorservice.util.HashUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class RequestDataExport224JobConfiguration(private val trainingFileRepository: TrainingFileRepository) {

    private val jobDefinition = JobDefinition(
        jobName = "RequestDataExport224Job",
        jobDescription = "Exports the requests data into a csv with a feature vector of size [224, 1].",
        cronExpression = "0 0 12 */7 * ?"
    )

    private val fileName = "trainingData224.csv.gz"

    @PostConstruct
    fun initFile() {
        trainingFileRepository.save(
            TrainingFile(
                HashUtils.sha256(fileName),
                fileName,
                "This file includes vectors of 224 features and 1 label at the end in a csv format."
            )
        )
    }

    @Bean
    fun requestDataExport224JobRunnable(
        minioService: MinioService,
        featureExtractor224: FeatureExtractor,
        requestDataService: RequestDataService
    ): AbstractJobRunnable {
        return RequestDataExportJob(jobDefinition, minioService, featureExtractor224, requestDataService, fileName)
    }
}
