package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.converter.RequestDataDtoToRequestDataConverter.convert
import com.trackingdetector.trackingdetectorservice.dto.RequestDataDto
import com.trackingdetector.trackingdetectorservice.repository.RequestDataRepository
import org.springframework.stereotype.Service

@Service
class RequestDataService(private val requestDataRepository: RequestDataRepository) {

    fun createRequestData(requestDataDto: RequestDataDto) {
        requestDataRepository.save(convert(requestDataDto))
    }

}