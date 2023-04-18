package com.trackingdetector.trackingdetectorservice.controller

import com.trackingdetector.trackingdetectorservice.service.MinioService
import org.apache.commons.compress.utils.IOUtils
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLConnection
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/tracking-detector")
class DownloadController(private val minioService: MinioService) {

    @GetMapping("/files/models")
    @CrossOrigin(origins = ["*"])
    fun getModels() {
    }

    @GetMapping("/files/trainingData")
    @CrossOrigin(origins = ["*"])
    fun getTrainingData() {
    }

    @GetMapping("/files/models/{folder}/{object}")
    @CrossOrigin(origins = ["*"])
    fun getObject(@PathVariable folder: String, @PathVariable `object`: String, response: HttpServletResponse) {
        val resourceStream = minioService.getModelFilesByFolderAndName(folder, `object`)
        response.addHeader("Content-disposition", "attachment;filename=$`object`")
        response.contentType = URLConnection.guessContentTypeFromName(`object`)
        IOUtils.copy(resourceStream, response.outputStream)
        response.flushBuffer()
    }
}
