package com.trackingdetector.trackingdetectorservice.testSupport

import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MockRpcHandler {

    var mockValueCreator: (() -> Map<*, *>)? = null

    fun training(applicationName: String, modelStorageName: String, trainingDataFilename: String, modelJson: String, batchSize: Int, epochs: Int): Map<*, *> {
        if (mockValueCreator == null) {
            return mapOf("accuracy" to 0.9, "log" to "Info", "trainingRun" to Instant.now().toEpochMilli().toInt())
        }
        return mockValueCreator!!.invoke()
    }
}
