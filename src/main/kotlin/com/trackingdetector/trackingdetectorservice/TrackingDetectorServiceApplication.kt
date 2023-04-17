package com.trackingdetector.trackingdetectorservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TrackingDetectorServiceApplication

fun main(args: Array<String>) {
    runApplication<TrackingDetectorServiceApplication>(*args)
}
