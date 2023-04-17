package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import com.trackingdetector.trackingdetectorservice.service.KerasModelService
import com.trackingdetector.trackingdetectorservice.service.TrainingResultService
import org.apache.xmlrpc.client.XmlRpcClient

class ModelTrainingJob(
    jobDefinition: JobDefinition,
    private val xmlRpcClient: XmlRpcClient,
    private val kerasModelService: KerasModelService,
    private val trainingResultService: TrainingResultService,
    private val methodName: String
) : JobRunnable(jobDefinition) {
    override fun execute(jobPublisher: JobPublisher): Boolean {
        val allModels = kerasModelService.getAllKerasModels()

        if (allModels.isEmpty()) {
            jobPublisher.info("No models found.")
            jobPublisher.skipped()
            return false
        }
        jobPublisher.info("Found ${allModels.size} Models to train.")
        for (model in allModels) {
            jobPublisher.info("Started training of ${model.modelStorageName}")
            val result = xmlRpcClient.execute(
                methodName,
                listOf(
                    model.applicationName,
                    model.modelStorageName,
                    model.trainingDataFilename,
                    model.modelJson,
                    model.batchSize,
                    model.epochs
                )
            ) as HashMap<*, *>

            jobPublisher.info(result["log"] as String)
            val trainingResult = TrainingResult.fromHashMap(model.id, result)
            jobPublisher.info("Finished training ${model.modelName} with an accuracy of: ${trainingResult.accuracy}.")
            trainingResultService.createTrainingResult(trainingResult)
            jobPublisher.info("Finished training of ${model.modelStorageName}")
        }

        return true
    }
}
