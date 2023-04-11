package com.trackingdetector.trackingdetectorservice.job

import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.repository.JobMetaRepository
import com.trackingdetector.trackingdetectorservice.repository.JobRunRepository
import com.trackingdetector.trackingdetectorservice.service.MinioService
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.zip.GZIPOutputStream

class RequestDataExportJob(jobDefinition: JobDefinition,
                           private val minioService: MinioService,
                           private val featureExtractor: FeatureExtractor,
                           private val requestDataService: RequestDataService)
    : JobRunnable(jobDefinition) {
    override fun execute(jobPublisher: JobPublisher): Boolean {
        jobPublisher.info("Starting Export...")
        val amountOfData = requestDataService.getNumberOfRequestData()

        if (amountOfData == 0L) {
            jobPublisher.warn("Did not find any request data in the database...")
            jobPublisher.skipped()
            return false
        }

        jobPublisher.info("Found $amountOfData RequestObjects in Database")
        val unZippedFilename = "temp.csv"
        val unZippedFile = File(unZippedFilename).printWriter(Charset.defaultCharset())
        var progess = 1
        this.requestDataService.streamRequestData().forEach {
            if (progess % 1000 == 0) {
                jobPublisher.info("Progress: $progess/$amountOfData")
            }
            unZippedFile.println(featureExtractor.extract(it)+"\n")
            unZippedFile.flush()
            progess++
        }
        unZippedFile.close()
        jobPublisher.info("Finished export to tmp File.")
        val zippedFilename = "training.csv.gz"
        File(unZippedFilename).inputStream().gzipCompress(zippedFilename)
        jobPublisher.info("Finished zipping File.")
        jobPublisher.info("Starting upload to bucket.")
        this.minioService.putCompressedTrainingData(zippedFilename)
        jobPublisher.info("Finished uploading compressed training data.")
        jobPublisher.info("Start cleaning up.")
        File(unZippedFilename).delete()
        File(zippedFilename).delete()
        jobPublisher.info("Finished Export.")

        return true

    }

    private fun FileInputStream.gzipCompress(toFile: String) {
        GZIPOutputStream(FileOutputStream(toFile)).bufferedWriter(Charset.defaultCharset()).use {
            it.write(this.readAllBytes().toString(Charset.defaultCharset()))
        }
    }

}