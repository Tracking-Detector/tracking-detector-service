package com.trackingdetector.trackingdetectorservice.configuration

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfiguration {

    @Bean()
    fun getMinioClient(
        @Value("\${minio.url}") minioUrl: String,
        @Value("\${minio.port}") minioPort: Int,
        @Value("\${minio.accessKey}") accessKey: String,
        @Value("\${minio.privateKey}") privateKey: String
    ): MinioClient {
        return MinioClient.builder()
            .endpoint(minioUrl, minioPort, false)
            .credentials(accessKey, privateKey)
            .build()
    }
}
