package com.trackingdetector.trackingdetectorservice.extractor

object FeatureExtractorUtils {
    private val TYPES: List<String> = listOf(
        "xmlhttprequest",
        "image",
        "font",
        "script",
        "stylesheet",
        "ping",
        "sub_frame",
        "other",
        "main_frame",
        "csp_report",
        "object",
        "media",
    )
    private val FRAME_TYPES : List<String> = listOf("outermost_frame", "fenced_frame", "sub_frame")

    private val METHODS: List<String> = listOf(
        "GET",
        "POST",
        "OPTIONS",
        "HEAD",
        "PUT",
        "DELETE",
        "SEARCH",
        "PATCH",
    )
    val URL_EXTRACTOR : (String) -> List<Int> = {
        var encoding: MutableList<Int> = mutableListOf()
        for (char in it.toCharArray()) {
            encoding.add((char.code % 89) + 1)
        }
        if (encoding.size < 200) {
            encoding = (MutableList(200 - encoding.size) {0} + encoding).toMutableList()
        } else if (encoding.size > 200) {
            encoding = encoding.subList(encoding.size - 200, encoding.size)
        }
        encoding.toList()
    }

    val FRAME_TYPE_EXTRACTOR: (String) -> List<Int> = {
        listOf(FRAME_TYPES.indexOf(it) + 1)
    }


    val METHOD_EXTRACTOR: (String) -> List<Int> = {
        listOf(METHODS.indexOf(it) + 1)
    }

    val TYPE_EXTRACTOR: (String) -> List<Int> = {
        listOf(TYPES.indexOf(it) + 1)
    }

    val REQUEST_HEADER_REFERER: (List<Map<String, String>>) -> List<Int> =  {
        var isReferred = 0;
        for (header in it) {
            if (header["Referer"] != null) {
                isReferred = 1
                break
            }
        }
        listOf(isReferred)
    }

    val LABEL_EXTRACTOR: (Boolean) -> List<Int> = {
        listOf(if (it) 1 else 0)
    }

}