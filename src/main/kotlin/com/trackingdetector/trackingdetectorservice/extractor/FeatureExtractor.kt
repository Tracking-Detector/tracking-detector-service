package com.trackingdetector.trackingdetectorservice.extractor

import com.trackingdetector.trackingdetectorservice.domain.RequestData


class FeatureExtractor private constructor(
    private val sequence: List<ExtractorTypes>,
    private val documentId: ((String) -> List<Int>)?,
    private val documentLifecycle: ((String) -> List<Int>)?,
    private val frameId: ((Int) -> List<Int>)?,
    private val frameType: ((String) -> List<Int>)?,
    private val initiator: ((String) -> List<Int>)?,
    private val method: ((String) -> List<Int>)?,
    private val parentFrameId: ((Int) -> List<Int>)?,
    private val requestId: ((String) -> List<Int>)?,
    private val tabId: ((Int) -> List<Int>)?,
    private val timeStamp: ((String) -> List<Int>)?,
    private val type: ((String) -> List<Int>)?,
    private val url: ((String) -> List<Int>)?,
    private val requestHeaders: ((List<Map<String, String>>) -> List<Int>)?,// TODO ADD RESPONSE
    private val success: ((Boolean) -> List<Int>)?,
    private val label: ((Boolean) -> List<Int>)?
) {

    companion object FeatureExtractor {

        fun builder() : FeatureExtractorBuilder {
            return FeatureExtractorBuilder()
        }

        enum class ExtractorTypes {
            DOCUMENT_ID, DOCUMENT_LIFECYCLE, FRAME_ID, FRAME_TYPE, INITIATOR, METHOD, PARENT_FRAME_ID, REQUEST_ID, TAB_ID, TIME_STAMP,
            TYPE, URL, REQUEST_HEADERS, SUCCESS, LABEL
        }

        class FeatureExtractorBuilder() {
            private var sequence: MutableList<ExtractorTypes> = mutableListOf()
            private var sequenceCount: Int = 0
            private var documentId: ((String) -> List<Int>)? = null
            private var documentLifecycle: ((String) -> List<Int>)? = null
            private var frameId: ((Int) -> List<Int>)? = null
            private var frameType: ((String) -> List<Int>)? = null
            private var initiator: ((String) -> List<Int>)? = null
            private var method: ((String) -> List<Int>)? = null
            private var parentFrameId: ((Int) -> List<Int>)? = null
            private var requestId: ((String) -> List<Int>)? = null
            private var tabId: ((Int) -> List<Int>)? = null
            private var timeStamp: ((String) -> List<Int>)? = null
            private var type: ((String) -> List<Int>)? = null
            private var url: ((String) -> List<Int>)? = null
            private var requestHeaders: ((List<Map<String, String>>) -> List<Int>)? = null

            // TODO ADD RESPONSE
            private var success: ((Boolean) -> List<Int>)? = null
            private var label: ((Boolean) -> List<Int>)? = null


            fun withDocumentIdExtractor(documentIdExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.documentId = documentIdExtractor
                this.sequence.add(ExtractorTypes.DOCUMENT_ID)
                return this
            }

            fun withDocumentLifecycleExtractor(documentLifecycleExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.documentLifecycle = documentLifecycleExtractor
                this.sequence.add(ExtractorTypes.DOCUMENT_LIFECYCLE)
                return this
            }

            fun withFrameIdExtractor(frameIdExtractor: (Int) -> List<Int>): FeatureExtractorBuilder {
                this.frameId = frameIdExtractor
                this.sequence.add(ExtractorTypes.FRAME_ID)
                return this
            }

            fun withFrameTypeExtractor(frameTypeExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.frameType = frameTypeExtractor
                this.sequence.add(ExtractorTypes.FRAME_TYPE)
                return this
            }

            fun withInitiatorExtractor(initiatorExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.initiator = initiatorExtractor
                this.sequence.add(ExtractorTypes.INITIATOR)
                return this
            }

            fun withMethodExtractor(methodExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.method = methodExtractor
                this.sequence.add(ExtractorTypes.METHOD)
                return this
            }

            fun withParentFrameIdExtractor(parentFrameIdExtractor: (Int) -> List<Int>): FeatureExtractorBuilder {
                this.parentFrameId = parentFrameIdExtractor
                this.sequence.add(ExtractorTypes.PARENT_FRAME_ID)
                return this
            }

            fun withRequestIdExtractor(requestIdExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.requestId = requestIdExtractor
                this.sequence.add(ExtractorTypes.REQUEST_ID)
                return this
            }

            fun withTabIdExtractor(tabIdExtractor: (Int) -> List<Int>): FeatureExtractorBuilder {
                this.tabId = tabIdExtractor
                this.sequence.add(ExtractorTypes.TAB_ID)
                return this
            }

            fun withTimeStampExtractor(timeStampExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.timeStamp = timeStampExtractor
                this.sequence.add(ExtractorTypes.TIME_STAMP)
                return this
            }

            fun withTypeExtractor(typeExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.type = typeExtractor
                this.sequence.add(ExtractorTypes.TYPE)
                return this
            }

            fun withUrlExtractor(urlExtractor: (String) -> List<Int>): FeatureExtractorBuilder {
                this.url = urlExtractor
                this.sequence.add(ExtractorTypes.URL)
                return this
            }

            fun withRequestHeadersExtractor(requestHeadersExtractor: (List<Map<String, String>>) -> List<Int>): FeatureExtractorBuilder {
                this.requestHeaders = requestHeadersExtractor
                this.sequence.add(ExtractorTypes.REQUEST_HEADERS)
                return this
            }


            fun withSuccessExtractor(successExtractor: (Boolean) -> List<Int>): FeatureExtractorBuilder {
                this.success = successExtractor
                this.sequence.add(ExtractorTypes.SUCCESS)
                return this
            }

            fun withLabelExtractor(labelExtractor: (Boolean) -> List<Int>): FeatureExtractorBuilder {
                this.label = labelExtractor
                this.sequence.add(ExtractorTypes.LABEL)
                return this
            }

            fun withSequence(sequence: List<ExtractorTypes>): FeatureExtractorBuilder {
                this.sequence = sequence.toMutableList()
                return this
            }

            fun build(): com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor {
                return FeatureExtractor(
                    this.sequence,
                    this.documentId,
                    this.documentLifecycle,
                    this.frameId,
                    this.frameType,
                    this.initiator,
                    this.method,
                    this.parentFrameId,
                    this.requestId,
                    this.tabId,
                    this.timeStamp,
                    this.type,
                    this.url,
                    this.requestHeaders,
                    this.success,
                    this.label
                )
            }
        }
    }

    private fun extractDocumentId(requestData: RequestData): List<Int> {
        if (documentId == null) {
            throw Exception()
        }
        return documentId.invoke(requestData.documentId)
    }

    private fun extractDocumentLifecycle(requestData: RequestData): List<Int> {
        if (documentLifecycle == null) {
            throw Exception()
        }
        return documentLifecycle.invoke(requestData.documentLifecycle)
    }

    private fun extractFrameId(requestData: RequestData): List<Int> {
        if (frameId == null) {
            throw Exception()
        }
        return frameId.invoke(requestData.frameId)
    }

    private fun extractFrameType(requestData: RequestData): List<Int> {
        if (frameType == null) {
            throw Exception()
        }
        return frameType.invoke(requestData.frameType)
    }

    private fun extractInitiator(requestData: RequestData): List<Int> {
        if (initiator == null) {
            throw Exception()
        }
        return initiator.invoke(requestData.initiator)
    }

    private fun extractMethod(requestData: RequestData): List<Int> {
        if (method == null) {
            throw Exception()
        }
        return method.invoke(requestData.method)
    }

    private fun extractParentFrameId(requestData: RequestData): List<Int> {
        if (parentFrameId == null) {
            throw Exception()
        }
        return parentFrameId.invoke(requestData.parentFrameId)
    }

    private fun extractRequestId(requestData: RequestData): List<Int> {
        if (requestId == null) {
            throw Exception()
        }
        return requestId.invoke(requestData.requestId)
    }

    private fun extractTabId(requestData: RequestData): List<Int> {
        if (tabId == null) {
            throw Exception()
        }
        return tabId.invoke(requestData.tabId)
    }

    private fun extractTimeStamp(requestData: RequestData): List<Int> {
        if (timeStamp == null) {
            throw Exception()
        }
        return timeStamp.invoke(requestData.timeStamp)
    }

    private fun extractType(requestData: RequestData): List<Int> {
        if (type == null) {
            throw Exception()
        }
        return type.invoke(requestData.type)
    }

    private fun extractUrl(requestData: RequestData): List<Int> {
        if (url == null) {
            throw Exception()
        }
        return url.invoke(requestData.url)
    }

    private fun extractRequestHeaders(requestData: RequestData): List<Int> {
        if (requestHeaders == null) {
            throw Exception()
        }
        return requestHeaders.invoke(requestData.requestHeaders)
    }

    private fun extractSuccess(requestData: RequestData): List<Int> {
        if (success == null) {
            throw Exception()
        }
        return success.invoke(requestData.success)
    }

    private fun extractLabel(requestData: RequestData): List<Int> {
        if (label == null) {
            throw Exception()
        }
        return label.invoke(requestData.label)
    }

    private fun extractVariable(requestData: RequestData, currentProperty: ExtractorTypes) : List<Int> {
        when(currentProperty) {
            ExtractorTypes.DOCUMENT_ID -> return extractDocumentId(requestData)
            ExtractorTypes.DOCUMENT_LIFECYCLE -> return extractDocumentLifecycle(requestData)
            ExtractorTypes.FRAME_ID -> return extractFrameId(requestData)
            ExtractorTypes.FRAME_TYPE -> return extractFrameType(requestData)
            ExtractorTypes.INITIATOR -> return extractInitiator(requestData)
            ExtractorTypes.METHOD-> return extractMethod(requestData)
            ExtractorTypes.PARENT_FRAME_ID -> return extractParentFrameId(requestData)
            ExtractorTypes.REQUEST_ID -> return extractRequestId(requestData)
            ExtractorTypes.TAB_ID-> return extractTabId(requestData)
            ExtractorTypes.TIME_STAMP-> return extractTimeStamp(requestData)
            ExtractorTypes.TYPE -> return extractType(requestData)
            ExtractorTypes.URL -> return extractUrl(requestData)
            ExtractorTypes.REQUEST_HEADERS -> return extractRequestHeaders(requestData)
            ExtractorTypes.SUCCESS -> return extractSuccess(requestData)
            ExtractorTypes.LABEL -> return extractLabel(requestData)
        }
    }

    fun extract(requestData: RequestData) : String {
        return this.sequence.map {
            extractVariable(requestData, it)
        }.reduce { acc: List<Int>, ints: List<Int> ->
            acc + ints
        }.joinToString(",")
    }
}