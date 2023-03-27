package com.trackingdetector.trackingdetectorservice.configuration

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfiguration {

    @Bean()
    fun getMinioClient() : MinioClient {
        return MinioClient.builder()
            .endpoint("minio", 8080, false)
            .credentials("asdadssad", "asdasdsad")
            .build()
    }

}