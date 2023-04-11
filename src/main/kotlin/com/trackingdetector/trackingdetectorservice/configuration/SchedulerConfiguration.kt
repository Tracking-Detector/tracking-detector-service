package com.trackingdetector.trackingdetectorservice.configuration

import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.Dsl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfiguration {
    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler()
    }

    @Bean
    fun asyncHttpClient(): AsyncHttpClient {
        return Dsl.asyncHttpClient()
    }

}