package com.gluck.jobtracker

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationRepository: JpaRepository<JobApplication, Long> {
    @Query("SELECT ja FROM JobApplication ja WHERE LOWER(ja.companyName) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    fun filterByCompany(typedName: String): List<JobApplication>
}