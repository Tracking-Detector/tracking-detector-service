package com.trackingdetector.trackingdetectorservice.acceptanceTest

import com.trackingdetector.trackingdetectorservice.domain.KerasModel
import com.trackingdetector.trackingdetectorservice.repository.KerasModelRepository
import com.trackingdetector.trackingdetectorservice.testSupport.AbstractSpringTest
import com.trackingdetector.trackingdetectorservice.testSupport.JobTest
import com.trackingdetector.trackingdetectorservice.testSupport.MinioTest
import com.trackingdetector.trackingdetectorservice.testSupport.MockXMLRPCServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

class ModelTrainingJobAcceptanceTest : AbstractSpringTest() {

    @Autowired
    private lateinit var jobTest: JobTest

    @Autowired
    private lateinit var mockRpcServer: MockXMLRPCServer

    @Autowired
    private lateinit var kerasModelRepository: KerasModelRepository

    @Autowired
    private lateinit var minioTest: MinioTest

    @BeforeEach
    fun setUp() {
        this.mockRpcServer.startServer()
        this.kerasModelRepository.deleteAll()
    }

    @AfterEach
    fun drillDown() {
        this.mockRpcServer.stopServer()
    }

    @Test
    fun should_be_skipped_when_no_models_available() {
        // given

        // when
        jobTest.startModelTrainingJob()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusSkipped()
        // then
    }

    @Test
    fun should_train_model_and_store_accuracy_in_db() {
        // given
        minioTest.putFileIntoBucket("training", "trainingData204.csv.gz")
        kerasModelRepository.save(
            KerasModel(
                "someId",
                "someModelName",
                "someModelDescription",
                "modelJson",
                10,
                10,
                "trainingData204.csv.gz",
                "trackingdetector",
                "204Model"
            )
        )

        val now = Instant.now().toEpochMilli()

        // when
        jobTest.startModelTrainingJob()
            .and()
            .waitForJobCompletion()
            .and()
            .jobFinishedWithStatusOk()
        // then

        print("Super duper")
    }
}
