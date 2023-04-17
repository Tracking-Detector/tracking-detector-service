package com.trackingdetector.trackingdetectorservice.dto

data class RequestDataDto(
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
    val requestHeaders: List<Map<String, String>>,
    val success: Boolean,
    val label: Boolean
)
