package com.trackingdetector.trackingdetectorservice.service

import com.trackingdetector.trackingdetectorservice.converter.RequestDataDtoToRequestDataConverter.convert
import com.trackingdetector.trackingdetectorservice.domain.RequestData
import com.trackingdetector.trackingdetectorservice.dto.RequestDataDto
import com.trackingdetector.trackingdetectorservice.repository.RequestDataRepository
import org.springframework.stereotype.Service
import java.util.stream.Stream

@Service
class RequestDataService(private val requestDataRepository: RequestDataRepository) {

    fun createRequestData(requestDataDto: RequestDataDto) {
        requestDataRepository.save(convert(requestDataDto))
    }

    fun getNumberOfRequestData(): Long {
        return requestDataRepository.count()
    }

    fun streamRequestData(): Stream<RequestData> {
        return this.requestDataRepository.streamAllBy()
    }
}
