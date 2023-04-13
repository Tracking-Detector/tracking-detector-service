package com.trackingdetector.trackingdetectorservice.acceptanceTest

import com.trackingdetector.trackingdetectorservice.testSupport.AbstractSpringTest
import com.trackingdetector.trackingdetectorservice.testSupport.JobTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ModelTraining204JobAcceptanceTest: AbstractSpringTest() {
    @Autowired
    private lateinit var jobTest: JobTest



}