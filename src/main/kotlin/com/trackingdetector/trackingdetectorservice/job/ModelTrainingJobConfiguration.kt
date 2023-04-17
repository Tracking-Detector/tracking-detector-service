package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.service.KerasModelService
import com.trackingdetector.trackingdetectorservice.service.TrainingResultService
import org.apache.xmlrpc.client.XmlRpcClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelTrainingJobConfiguration {
    val jobDefinition = JobDefinition(
        jobName = "ModelTrainingJob",
        jobDescription = "Trains a model based on the exported csv data of the RequestExport204Job",
        cronExpression = "0 0 12 */7 * ?"
    )

    @Bean
    fun modelTrainingJobRunnable(
        xmlRpcClient: XmlRpcClient,
        kerasModelService: KerasModelService,
        trainingResultService: TrainingResultService,
        @Value("\${rpc.method}") methodName: String
    ): JobRunnable {
        return ModelTrainingJob(jobDefinition, xmlRpcClient, kerasModelService, trainingResultService, methodName)
    }
}
