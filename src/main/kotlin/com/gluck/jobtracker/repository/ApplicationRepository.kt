package com.gluck.jobtracker.repository

import com.gluck.jobtracker.model.JobApplicationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationRepository: JpaRepository<JobApplicationEntity, Long> {
    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.companyName) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun filterByCompany(typedName: String): List<JobApplicationEntity>
}