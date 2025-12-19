package com.gluck.jobtracker

import com.gluck.JobApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository: JpaRepository<JobApplication, Long> {
}