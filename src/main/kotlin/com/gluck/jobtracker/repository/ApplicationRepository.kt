package com.gluck.jobtracker.repository

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationRepository: JpaRepository<JobApplicationEntity, Long> {
    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.companyName) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun searchByCompany(typedName: String, pageable: Pageable): Page<JobApplicationEntity>

    fun countByCompanyNameContainsIgnoreCase(companyName: String): Long

//    @Query("SELECT ja.status, COUNT(ja.status) FROM JobApplicationEntity ja GROUP BY ja.status ORDER BY COUNT(ja.status)")
//    fun countAllByStatus(): HashMap<Status, Int>
}