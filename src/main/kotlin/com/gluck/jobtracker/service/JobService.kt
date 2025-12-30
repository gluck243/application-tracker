package com.gluck.jobtracker.service

import com.gluck.jobtracker.model.JobApplicationRequest
import com.gluck.jobtracker.model.JobApplicationResponse
import com.gluck.jobtracker.repository.ApplicationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
@Transactional(rollbackFor = [Exception::class])
class JobService(private val repository: ApplicationRepository, private val mapper: ApplicationMapper) {

    fun getAllJobs(): List<JobApplicationResponse> {
        val entities = repository.findAll()
        val responses = entities.map { entity -> mapper.toResponse(entity) }
        return responses
    }

    fun findJobsByCompanyName(keyword: String): List<JobApplicationResponse> {
        val entities = repository.filterByCompany(keyword)
        val responses = entities.map { entity -> mapper.toResponse(entity) }
        return responses
    }

    fun saveJob(request: JobApplicationRequest): Long {
        val job = mapper.toEntity(request)
        val newEntity = repository.save(job)
        return newEntity.id
    }

    fun updateJob(id: Long, request: JobApplicationRequest) {
        val entity = repository.findById(id).orElseThrow()
        entity.apply {
            position = request.position
            companyName = request.companyName
            status = request.status
            dateApplied = request.dateApplied
            description = request.description
        }
        repository.save(entity)
    }

    fun deleteJob(id: Long) {
        repository.deleteById(id)
    }

    fun findJob(id: Long): JobApplicationRequest {
        val entity = repository.findById(id).orElseThrow()
        return mapper.toRequest(entity)
    }

}