package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.domain.TrainingResult
import com.trackingdetector.trackingdetectorservice.service.KerasModelService
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.TrainingFileService
import com.trackingdetector.trackingdetectorservice.service.TrainingResultService
import com.trackingdetector.trackingdetectorservice.util.HashUtils
import org.apache.xmlrpc.client.XmlRpcClient

class ModelTrainingJob(
    jobDefinition: JobDefinition,
    private val xmlRpcClient: XmlRpcClient,
    private val kerasModelService: KerasModelService,
    private val trainingResultService: TrainingResultService,
    private val trainingFileService: TrainingFileService,
    private val minioService: MinioService,
    private val methodName: String
) : AbstractJobRunnable(jobDefinition) {
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

            val trainingFile = trainingFileService.getFileById(HashUtils.sha256(model.trainingDataFilename))
            if (trainingFile.isEmpty) {
                jobPublisher.warn("The specified training file ${model.trainingDataFilename} is not available. Cannot train model without data.")
                continue
            }
            if (!minioService.isTrainingFileAvailable(trainingFile.get().fileName)) {
                jobPublisher.warn("The specified training file ${model.trainingDataFilename} is not available in the minio bucket. Cannot train model without data.")
                continue
            }

            val result = xmlRpcClient.execute(
                methodName,
                listOf(
                    model.applicationName,
                    model.modelStorageName,
                    model.trainingDataFilename,
                    model.modelJson,
                    trainingFile.get().vectorLength,
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
