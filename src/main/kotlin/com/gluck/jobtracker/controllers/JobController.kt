package com.gluck.jobtracker.controllers

import com.gluck.jobtracker.model.JobApplication
import com.gluck.jobtracker.service.JobService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class JobController(private val service: JobService){

    @GetMapping("/jobs")
    fun getAllApplications(): ResponseEntity<List<JobApplication>> {
        val applications = service.getAllJobs()
        return ResponseEntity<JobApplication>.ok(applications)
    }


}