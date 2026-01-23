package com.gluck.jobtracker.repository

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationRepository: JpaRepository<JobApplicationEntity, Long> {
    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.position) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun searchByPosition(typedName: String, pageable: Pageable): Page<JobApplicationEntity>
    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.companyName) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun searchByCompany(typedName: String, pageable: Pageable): Page<JobApplicationEntity>
    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(CAST(ja.description AS STRING)) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun searchByDescription(typedName: String, pageable: Pageable): Page<JobApplicationEntity>

    fun countByPositionContainsIgnoreCase(position: String): Long
    fun countByCompanyNameContainsIgnoreCase(companyName: String): Long
    @Query("SELECT COUNT(ja) FROM JobApplicationEntity ja WHERE UPPER(CAST(ja.description AS STRING)) LIKE UPPER(CONCAT('%', :desc, '%'))")
    fun countByDescriptionContainsIgnoreCase(desc: String): Long

//    @Query("SELECT ja.status, COUNT(ja.status) FROM JobApplicationEntity ja GROUP BY ja.status ORDER BY COUNT(ja.status)")
//    fun countAllByStatus(): HashMap<Status, Int>
}