package com.gluck.jobtracker.service

import com.gluck.jobtracker.model.JobApplication
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
        val applications = mutableListOf<JobApplicationResponse>()
        val applicationRecords = repository.findAll()
        for (application in applicationRecords) {
            applications.add(mapper.toDTO(application))
        }
        return applications
    }

    fun findJobsByCompanyName(keyword: String): List<JobApplication> {
        return repository.filterByCompany(keyword)
    }

    fun saveJob(job: JobApplication) {
        repository.save(job)
    }

    fun deleteJob(job: JobApplication) {
        repository.delete(job)
    }

    fun getJobForEdit(id: Long): JobApplicationRequest {
        val entity = repository.findById(id).orElseThrow()
        return mapper.toRequest(entity)
    }

}