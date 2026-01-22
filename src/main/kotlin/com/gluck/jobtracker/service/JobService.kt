package com.gluck.jobtracker.service

import com.gluck.jobtracker.exception.NoSuchJobFoundException
import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.repository.ApplicationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
@Transactional(rollbackFor = [Exception::class])
class JobService(private val repository: ApplicationRepository, private val mapper: ApplicationMapper) {

    fun getJobs(pageable: Pageable, filter: String?): Page<JobApplicationResponse> {
        val page = if (filter.isNullOrBlank()) {
            repository.findAll(pageable)
        }
        else {
            repository.searchByCompany(filter, pageable)
        }
        return page.map { mapper.toResponse(it) }
    }

    fun findJobsByCompanyName(keyword: String, pageable: Pageable): Page<JobApplicationResponse> {
        val entities = repository.searchByCompany(keyword, pageable)
        val responses = entities.map { entity -> mapper.toResponse(entity) }
        return responses
    }

    fun countJobs(filter: String?): Long {
        return if (filter.isNullOrBlank())
            repository.count()
        else
            repository.countByCompanyNameContainsIgnoreCase(filter)
    }

    fun saveJob(request: JobApplicationRequest): Long {
        val job = mapper.toEntity(request)
        val newEntity = repository.save(job)
        return newEntity.id
    }

    fun updateJobById(id: Long, request: JobApplicationRequest): JobApplicationResponse {
        val entity = repository.findById(id).orElseThrow{ NoSuchJobFoundException("No matching job found for $id") }
        entity.apply {
            position = request.position
            companyName = request.companyName
            status = request.status
            dateApplied = request.dateApplied
            description = request.description
        }
        val updatedEntity = repository.save(entity)
        return mapper.toResponse(updatedEntity)
    }

    fun deleteJob(id: Long) {
        repository.findById(id).orElseThrow{ NoSuchJobFoundException("No matching job found for $id") }
        repository.deleteById(id)
    }

    fun findJobForEditing(id: Long): JobApplicationRequest {
        val entity = repository.findById(id).orElseThrow{ NoSuchJobFoundException("No matching job found for $id") }
        return mapper.toRequest(entity)
    }

    fun findJobById(id: Long): JobApplicationResponse{
        val entity = repository.findById(id).orElseThrow{ NoSuchJobFoundException("No matching job found for $id") }
        return mapper.toResponse(entity)
    }

}