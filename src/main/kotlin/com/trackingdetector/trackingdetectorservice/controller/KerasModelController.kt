package com.trackingdetector.trackingdetectorservice.controller

import com.trackingdetector.trackingdetectorservice.converter.KerasModelToKerasModelRepresentationConverter.convert
import com.trackingdetector.trackingdetectorservice.dto.KerasModelDto
import com.trackingdetector.trackingdetectorservice.representation.KerasModelRepresentation
import com.trackingdetector.trackingdetectorservice.service.KerasModelService
import com.trackingdetector.trackingdetectorservice.service.TrainingResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/models")
class KerasModelController(private val kerasModelService: KerasModelService, private val trainingResultService: TrainingResultService) {


    @GetMapping("")
    fun getAllModels() : List<KerasModelRepresentation> {
       return kerasModelService.getAllKerasModels().map {
           val runs = trainingResultService.getAllTrainingResultsForModel(it.id)
           convert(it, runs)
       }.toList()
    }

    @GetMapping("{id}")
    fun getModelById(@PathVariable id: String) : KerasModelRepresentation {
        try {
            val kerasModel = kerasModelService.getKerasModelById(id)
            val trainingRuns = trainingResultService.getAllTrainingResultsForModel(kerasModel.id)
            return convert(kerasModel, trainingRuns)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PostMapping("")
    fun createModel(@RequestBody kerasModelDto: KerasModelDto): ResponseEntity<String> {
        try {
            kerasModelService.createKerasModel(kerasModelDto)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


    @PutMapping("{id}")
    fun updateModel(@RequestBody kerasModelDto: KerasModelDto, @PathVariable id: String): ResponseEntity<String> {
        try {
            kerasModelService.createKerasModel(kerasModelDto)
            return ResponseEntity.status(HttpStatus.CREATED).build()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }


}