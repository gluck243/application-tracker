package com.gluck.jobtracker.service

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import org.springframework.stereotype.Component

@Component
class ApplicationMapper {

    fun toEntity(request: JobApplicationRequest): JobApplicationEntity {
        return JobApplicationEntity(
            position = request.position,
            companyName = request.companyName,
            status = request.status,
            dateApplied = request.dateApplied,
            description = request.description
        )
    }

    fun toResponse(application: JobApplicationEntity): JobApplicationResponse {
        return JobApplicationResponse(
            id = application.id,
            position = application.position,
            companyName = application.companyName,
            status = application.status,
            dateApplied = application.dateApplied,
            description = application.description
        )
    }

    fun toRequest(application: JobApplicationEntity): JobApplicationRequest {
        return JobApplicationRequest(
            position = application.position,
            companyName = application.companyName,
            status = application.status,
            dateApplied = application.dateApplied,
            description = application.description
        )
    }

}