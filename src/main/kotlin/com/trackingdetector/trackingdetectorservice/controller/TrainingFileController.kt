package com.trackingdetector.trackingdetectorservice.controller

import com.trackingdetector.trackingdetectorservice.domain.TrainingFile
import com.trackingdetector.trackingdetectorservice.service.TrainingFileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tracking-detector")
class TrainingFileController(private val trainingFileService: TrainingFileService) {

    @GetMapping("/trainingFiles")
    fun getAllTrainingFiles(): List<TrainingFile> {
        return trainingFileService.getAllTrainingFiles()
    }
}
