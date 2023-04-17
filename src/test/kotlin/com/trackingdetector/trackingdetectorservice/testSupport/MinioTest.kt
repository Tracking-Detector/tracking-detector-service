package com.trackingdetector.trackingdetectorservice.testSupport

import io.minio.GetObjectArgs
import io.minio.MinioClient
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

@Component
class MinioTest(private val minioClient: MinioClient) {

    fun getContentOfFile(bucketName: String, objectName: String): String {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(objectName)
                .build()
        ).readAllBytes().gzipDecompress()
    }

    private fun ByteArray.gzipDecompress(): String {
        val bais = ByteArrayInputStream(this)
        lateinit var string: String
        GZIPInputStream(bais).bufferedReader(Charsets.UTF_8).use { return it.readText() }
    }
}
