package com.trackingdetector.trackingdetectorservice.job


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ModelTrainingJobConfiguration {
    val jobDefinition = JobDefinition(
        jobName = "ModelTraining204Job",
        jobDescription = "Trains a model based on the exported csv data of the RequestExport204Job",
        cronExpression = "0 0 12 */7 * ?"
    )


    @Bean
    fun modelTrainingJobRunnable(): JobRunnable {

        return ModelTrainingJob(jobDefinition)
    }

}