package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.service.KerasModelService
import org.apache.xmlrpc.client.XmlRpcClient


class ModelTrainingJob(jobDefinition: JobDefinition,
                       private val xmlRpcClient: XmlRpcClient,
                       private val kerasModelService: KerasModelService) : JobRunnable(jobDefinition) {
    override fun execute(jobPublisher: JobPublisher): Boolean {
        val allModels = kerasModelService.getAllKerasModels()

        for (model in allModels) {
           jobPublisher.info("Started training of ${model.modelStorageName}")
           val result = xmlRpcClient.execute("training", listOf(model.applicationName,
               model.modelStorageName,
               model.trainingDataFilename,
               model.modelJson,
               model.batchSize,
               model.epochs)) as HashMap<*,*>

           jobPublisher.info(result["log"] as String)


           jobPublisher.info("Finished training of ${model.modelStorageName}")
        }

        return true
    }
}