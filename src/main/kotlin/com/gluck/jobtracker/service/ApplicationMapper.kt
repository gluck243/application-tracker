package com.gluck.jobtracker.service

import com.gluck.jobtracker.model.JobApplication
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import org.springframework.stereotype.Component

@Component
class ApplicationMapper {

    fun toEntity(request: JobApplicationRequest): JobApplication {
        return JobApplication(
            position = request.position,
            companyName = request.companyName,
            status = request.status,
            dateApplied = request.dateApplied,
            description = request.description
        )
    }

    fun toResponse(application: JobApplication): JobApplicationResponse {
        return JobApplicationResponse(
            id = application.id,
            position = application.position,
            companyName = application.companyName,
            status = application.status,
            dateApplied = application.dateApplied,
            description = application.description
        )
    }

    fun toRequest(application: JobApplication): JobApplicationRequest {
        return JobApplicationRequest(
            position = application.position,
            companyName = application.companyName,
            status = application.status,
            dateApplied = application.dateApplied,
            description = application.description
        )
    }

}