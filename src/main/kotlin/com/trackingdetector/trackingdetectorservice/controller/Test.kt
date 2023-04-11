package com.trackingdetector.trackingdetectorservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class Test {
    @GetMapping("/")
    fun test(): String {
        return "Hallo Welt"
    }
}
