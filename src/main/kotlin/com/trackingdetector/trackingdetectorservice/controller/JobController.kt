package com.trackingdetector.trackingdetectorservice.controller

import com.trackingdetector.trackingdetectorservice.domain.JobMeta
import com.trackingdetector.trackingdetectorservice.domain.JobRun
import com.trackingdetector.trackingdetectorservice.service.JobService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tracking-detector")
class JobController(private val jobService: JobService) {

    @GetMapping("/jobs")
    @CrossOrigin(origins = ["*"])
    fun getAllJobs(): List<JobMeta> {
        return this.jobService.getAllJobs()
    }

    @GetMapping("/jobs/{id}")
    @CrossOrigin(origins = ["*"])
    fun getJobById(@PathVariable id: String): JobMeta? {
        return this.jobService.getJobMetaById(id)
    }

    @PatchMapping("/jobs/{id}/toggle")
    @CrossOrigin(origins = ["*"])
    fun toggleJob(@PathVariable id: String) {
        this.jobService.toggleJobById(id)
    }

    @PostMapping("/jobs/{id}/trigger")
    @CrossOrigin(origins = ["*"])
    fun triggerJob(@PathVariable id: String): ResponseEntity<String> {
        val triggerResult = this.jobService.triggerJobById(id)
        return ResponseEntity.status(if (triggerResult) 204 else 404).build()
    }

    @GetMapping("/jobs/{id}/isActive")
    fun jobIsActive(@PathVariable id: String): Boolean {
        return this.jobService.jobIsActive(id)
    }

    @GetMapping("/jobs/{id}/runs")
    fun getAllRunsForJob(@PathVariable id: String): List<JobRun> {
        return this.jobService.getAllJobRunsForJob(id)
    }
}
