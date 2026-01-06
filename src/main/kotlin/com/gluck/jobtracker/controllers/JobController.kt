package com.gluck.jobtracker.controllers

import com.gluck.jobtracker.exception.NoSuchJobFoundException
import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.service.JobService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api")
class JobController(private val service: JobService){

    @GetMapping("/jobs")
    fun getAllApplications(): ResponseEntity<List<JobApplicationResponse>> {
        val applications = service.getAllJobs()
        return ResponseEntity<JobApplicationEntity>.ok(applications)
    }

    @PostMapping("/jobs")
    fun createJob(@RequestBody @Valid dto: JobApplicationRequest): ResponseEntity<Unit> {
        val newId = service.saveJob(dto)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newId)
            .toUri()

        return ResponseEntity<Unit>.created(location).build()
    }

    @GetMapping("/jobs/{id}")
    fun getJobById(@PathVariable id: Long): ResponseEntity<JobApplicationResponse> {
        val foundEntity = service.findJobById(id)
        return ResponseEntity<JobApplicationResponse>.ok(foundEntity)
    }

    @PutMapping("/jobs/{id}")
    fun updateJobById(@PathVariable id: Long, @RequestBody @Valid dto: JobApplicationRequest): ResponseEntity<JobApplicationResponse> {
        val updatedEntity = service.updateJobById(id, dto)
        return ResponseEntity<JobApplicationResponse>.ok(updatedEntity)
    }

}