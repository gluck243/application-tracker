package com.gluck.jobtracker.controllers

import com.gluck.jobtracker.model.ApiErrorResponse
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.service.JobService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api")
class JobController(private val service: JobService) {

    @Operation(
        summary = "Get All Job Applications",
        description = "Lists all available jobs applications"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK. Job Applications Found"),
    ])
    @GetMapping("/jobs")
    fun getAllApplications(): ResponseEntity<List<JobApplicationResponse>> {
        val applications = service.getAllJobs()
        return ResponseEntity.ok(applications)
    }

    @Operation(
        summary = "Get a Job Application By Id",
        description = "Lists a Job Application found by valid id"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK. Job Application by provided id found", content = [
            Content(mediaType = "application/json",
                schema = Schema(implementation = JobApplicationResponse::class))
        ]),
        ApiResponse(responseCode = "404", description = "Not Found. Job Application by provided id not found", content = [
            Content(mediaType = "application/json",
                schema = Schema(implementation = ApiErrorResponse::class))
        ])
    ])
    @GetMapping("/jobs/{id}")
    fun getJobById(@PathVariable id: Long): ResponseEntity<JobApplicationResponse> {
        val foundEntity = service.findJobById(id)
        return ResponseEntity.ok(foundEntity)
    }

    @Operation(
        summary = "Add a new Job Application Record",
        description = "Add information about a new job application. Make sure information is entered in a correct format",
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created. Job Applications created", content = [
            Content(mediaType = "text/plain", schema =
                Schema(type = "string", example = "/api/jobs/3"))]),
        ApiResponse(responseCode = "400", description = "Bad Request. Check request correctness"),
        ApiResponse(responseCode = "403", description = "Forbidden. Please log in and attempt the request again")
    ])
    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/jobs")
    fun createJob(@RequestBody @Valid dto: JobApplicationRequest): ResponseEntity<Unit> {
        val newId = service.saveJob(dto)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newId)
            .toUri()

        return ResponseEntity.created(location).build()
    }

    @Operation(
        summary = "Update a Job Application By Id",
        description = "Updates a Job Application by a valid id. Id and Request Body must be valid"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK. Job Application by provided id is found and updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = JobApplicationResponse::class))
            ]),
        ApiResponse(responseCode = "400", description = "Bad request. Check correctness of provided request"),
        ApiResponse(responseCode = "403", description = "Forbidden. Please log in and attempt the request again"),
        ApiResponse(responseCode = "404", description = "Not Found. Job Application by provided id is not found and wasn't updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ApiErrorResponse::class))])
    ])
    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/jobs/{id}")
    fun updateJobById(@PathVariable id: Long, @RequestBody @Valid dto: JobApplicationRequest): ResponseEntity<JobApplicationResponse> {
        val updatedEntity = service.updateJobById(id, dto)
        return ResponseEntity.ok(updatedEntity)
    }

    @Operation(
        summary = "Delete Job Application by id",
        description = "Permanently deletes a record from the database. Id must be valid"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "No content. Job Application successfully deleted"),
        ApiResponse(responseCode = "403", description = "Forbidden. Please log in and attempt the request again"),
        ApiResponse(responseCode = "404", description = "Not found. Job Application by the provided id was not found. " +
                "Check the validity of the id", content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ApiErrorResponse::class))])
    ])
    @SecurityRequirement(name = "basicAuth")
    @DeleteMapping("jobs/{id}")
    fun deleteJobById(@PathVariable id: Long): ResponseEntity<Unit> {
        service.deleteJob(id)
        return ResponseEntity.noContent().build()
    }

}