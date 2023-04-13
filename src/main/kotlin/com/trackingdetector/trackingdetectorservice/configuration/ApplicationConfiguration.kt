package com.trackingdetector.trackingdetectorservice.configuration

import com.trackingdetector.trackingdetectorservice.domain.Application
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun trackingDetectorApplication() : Application {
        return Application("tracking-detector")
    }
}