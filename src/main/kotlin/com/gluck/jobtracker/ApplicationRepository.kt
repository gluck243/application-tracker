package com.gluck.jobtracker

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository: JpaRepository<JobApplication, Long> {
}