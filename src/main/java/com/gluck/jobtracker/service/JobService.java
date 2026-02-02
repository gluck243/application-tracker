package com.gluck.jobtracker.service;

import com.gluck.jobtracker.exception.NoSuchJobFoundException;
import com.gluck.jobtracker.model.JobApplicationRequest;
import com.gluck.jobtracker.model.JobApplicationResponse;
import com.gluck.jobtracker.model.Status;
import com.gluck.jobtracker.repository.ApplicationRepository;
import com.gluck.jobtracker.repository.JobApplicationEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {

    private final ApplicationRepository repository;
    private final ApplicationMapper mapper;

    public Page<JobApplicationResponse> getJobs(Pageable pageable, String searchTerm, String searchBy) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return repository.findAll(pageable).map(mapper::toResponse);
        }

        Page<JobApplicationEntity> page;
        if ("Position".equalsIgnoreCase(searchBy)) {
            page = repository.searchByPosition(searchTerm, pageable);
        } else if ("Description".equalsIgnoreCase(searchBy)) {
            page = repository.searchByDescription(searchTerm, pageable);
        } else {
            page = repository.searchByCompany(searchTerm, pageable);
        }

        return page.map(mapper::toResponse);
    }

    public Page<JobApplicationResponse> findJobsByCompanyName(String keyword, Pageable pageable) {
        return repository.searchByCompany(keyword, pageable).map(mapper::toResponse);
    }

    public Long countJobs(String searchTerm, String searchBy) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return repository.count();
        }
        if ("Position".equalsIgnoreCase(searchBy)) {
            return repository.countByPositionContainsIgnoreCase(searchTerm);
        } else if ("Description".equalsIgnoreCase(searchBy)) {
            return repository.countByDescriptionContainsIgnoreCase(searchTerm);
        } else {
            return repository.countByCompanyNameContainsIgnoreCase(searchTerm);
        }
    }

    public Long saveJob(JobApplicationRequest request) {
        var job = mapper.toEntity(request);
        var newEntity = repository.save(job);
        return newEntity.getId();
    }

    public JobApplicationResponse updateJobById(Long id, JobApplicationRequest request) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchJobFoundException("No matching job found for " + id));

        entity.setPosition(request.getPosition());
        entity.setCompanyName(request.getCompanyName());
        entity.setStatus(request.getStatus());
        entity.setDateApplied(request.getDateApplied());
        entity.setDescription(request.getDescription());

        var updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }

    public void deleteJob(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchJobFoundException("No matching job found for " + id));
        repository.deleteById(entity.getId());
    }

    public JobApplicationRequest findJobForEditing(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchJobFoundException("No matching job found for " + id));
        return mapper.toRequest(entity);
    }

    public JobApplicationResponse findJobById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchJobFoundException("No matching job found for " + id));
        return mapper.toResponse(entity);
    }

    public Long countByStatus(Status status) {
        return repository.countByStatusIs(status);
    }
}

