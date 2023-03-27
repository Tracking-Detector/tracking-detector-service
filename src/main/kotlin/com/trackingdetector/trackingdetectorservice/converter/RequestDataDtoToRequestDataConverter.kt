package com.trackingdetector.trackingdetectorservice.converter

import com.trackingdetector.trackingdetectorservice.domain.RequestData
import com.trackingdetector.trackingdetectorservice.dto.RequestDataDto

object RequestDataDtoToRequestDataConverter {

    fun convert(requestDataDto: RequestDataDto) : RequestData {
        return RequestData(
            documentId = requestDataDto.documentId,
            requestId = requestDataDto.requestId,
            frameId = requestDataDto.frameId,
            frameType = requestDataDto.frameType,
            documentLifecycle = requestDataDto.documentLifecycle,
            label = requestDataDto.label,
            initiator = requestDataDto.initiator,
            method = requestDataDto.method,
            parentFrameId = requestDataDto.parentFrameId,
            success = requestDataDto.success,
            requestHeaders = requestDataDto.requestHeaders,
            tabId = requestDataDto.tabId,
            timeStamp = requestDataDto.timeStamp,
            type = requestDataDto.type,
            url = requestDataDto.url
        )
    }
}