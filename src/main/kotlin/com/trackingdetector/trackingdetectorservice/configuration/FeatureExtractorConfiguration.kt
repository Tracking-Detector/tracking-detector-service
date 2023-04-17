package com.trackingdetector.trackingdetectorservice.configuration

import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractor
import com.trackingdetector.trackingdetectorservice.extractor.FeatureExtractorUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureExtractorConfiguration {

    @Bean
    fun featureExtractor204(): FeatureExtractor {
        return FeatureExtractor.builder()
            .withUrlExtractor(FeatureExtractorUtils.URL_EXTRACTOR)
            .withFrameTypeExtractor(FeatureExtractorUtils.FRAME_TYPE_EXTRACTOR)
            .withMethodExtractor(FeatureExtractorUtils.METHOD_EXTRACTOR)
            .withTypeExtractor(FeatureExtractorUtils.TYPE_EXTRACTOR)
            .withRequestHeadersExtractor(FeatureExtractorUtils.REQUEST_HEADER_REFERER)
            .withLabelExtractor(FeatureExtractorUtils.LABEL_EXTRACTOR)
            .build()
    }
}
