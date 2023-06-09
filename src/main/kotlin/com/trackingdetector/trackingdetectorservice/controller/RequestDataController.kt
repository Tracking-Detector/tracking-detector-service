package com.trackingdetector.trackingdetectorservice.controller

import com.trackingdetector.trackingdetectorservice.dto.RequestDataDto
import com.trackingdetector.trackingdetectorservice.service.RequestDataService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tracking-detector")
class RequestDataController(private val requestDataService: RequestDataService) {

    @PostMapping("/requests", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin(origins = ["*"])
    fun addRequestData(@RequestBody requestDataDto: RequestDataDto) {
        this.requestDataService.createRequestData(requestDataDto)
    }
}
