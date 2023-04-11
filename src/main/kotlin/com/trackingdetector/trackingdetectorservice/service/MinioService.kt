package com.trackingdetector.trackingdetectorservice.service

import io.minio.*
import org.springframework.stereotype.Service
import io.minio.messages.VersioningConfiguration
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.InputStream
import javax.annotation.PostConstruct

@Service
class MinioService(val minioClient: MinioClient,
        @Value("\${buckets.trainingDataBucket}") val trainingDataBucket: String,
        @Value("\${buckets.modelBucket}") val modelBucket: String,
        @Value("\${buckets.mongoDbBackupBucket}") val mongoDbBackupBucket: String) {


    private fun initBucket(name: String) {
        val bucketExists = this.minioClient.bucketExists(BucketExistsArgs.builder()
            .bucket(name)
            .build())
        if (bucketExists) {
            return
        }
        this.minioClient.makeBucket(MakeBucketArgs.builder()
            .bucket(name)
            .build())
        this.minioClient.setBucketVersioning(SetBucketVersioningArgs.builder()
            .bucket(name)
            .config(VersioningConfiguration(VersioningConfiguration.Status.ENABLED, null))
            .build())
    }

    @PostConstruct
    private fun initBuckets() {
        this.initBucket(this.trainingDataBucket)
        this.initBucket(this.mongoDbBackupBucket)
        this.initBucket(this.modelBucket)
    }

    fun putCompressedTrainingData(filePath: String) {

       this.minioClient.uploadObject(UploadObjectArgs.builder()
           .bucket(this.trainingDataBucket)
           .`object`("training-data.csv.gz")
           .filename(filePath)
           .build())
    }

    fun getModelFilesByFolderAndName(modelFolder: String, fileName: String): GetObjectResponse? {
        return this.minioClient.getObject(GetObjectArgs.builder()
            .bucket(this.modelBucket)
            .`object`("$modelFolder/$fileName")
            .build()
        )
    }






}