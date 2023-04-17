package com.trackingdetector.trackingdetectorservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = ["com.trackingdetector.trackingdetectorservice"])
class TestApplication

fun main(args: Array<String>) {
    (runApplication<TestApplication>(*args))
}
