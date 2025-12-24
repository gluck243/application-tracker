package com.gluck.jobtracker

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
@Transactional(rollbackFor = [Exception::class])
class JobService(private val repository: ApplicationRepository) {

    fun getAllJobs(): List<JobApplication> {
        return repository.findAll()
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

}