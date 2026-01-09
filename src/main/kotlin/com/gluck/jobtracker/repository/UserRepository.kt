package com.gluck.jobtracker.repository

import com.gluck.jobtracker.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String?): UserEntity?
}