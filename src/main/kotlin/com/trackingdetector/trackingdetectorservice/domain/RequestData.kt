package com.trackingdetector.trackingdetectorservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


@Document
data class RequestData(
    @Id
    val id: UUID = UUID.randomUUID(),
    val documentId: String,
    val documentLifecycle: String,
    val frameId: Int,
    val frameType: String,
    val initiator: String,
    val method: String,
    val parentFrameId: Int,
    val requestId: String,
    val tabId: Int,
    val timeStamp: String,
    val type: String,
    val url: String,
    val requestHeaders: List<Map<String,String>>,
    val success: Boolean,
    val label: Boolean,
    )
