package com.gluck.jobtracker.service;

import com.gluck.jobtracker.repository.JobApplicationEntity;
import com.gluck.jobtracker.model.JobApplicationRequest;
import com.gluck.jobtracker.model.JobApplicationResponse;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public JobApplicationEntity toEntity(JobApplicationRequest request) {
        JobApplicationEntity entity = new JobApplicationEntity();
        entity.setPosition(request.getPosition());
        entity.setCompanyName(request.getCompanyName());
        entity.setStatus(request.getStatus());
        entity.setDateApplied(request.getDateApplied());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public JobApplicationResponse toResponse(JobApplicationEntity application) {
        return new JobApplicationResponse(
            application.getId(),
            application.getPosition(),
            application.getCompanyName(),
            application.getStatus(),
            application.getDateApplied(),
            application.getDescription()
        );
    }

    public JobApplicationRequest toRequest(JobApplicationEntity application) {
        return new JobApplicationRequest(
            application.getPosition(),
            application.getCompanyName(),
            application.getStatus(),
            application.getDateApplied(),
            application.getDescription()
        );
    }
}

