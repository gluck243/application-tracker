package com.gluck.jobtracker.service

import com.gluck.jobtracker.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {

        val safeUsername = username // ?: throw UsernameNotFoundException("Username is null")

        val userEntity = repository.findByUsername(safeUsername)
            ?: throw UsernameNotFoundException("User by $safeUsername not found")

        return User.withUsername(userEntity.username)
            .password(userEntity.password)
            .authorities(SimpleGrantedAuthority(userEntity.role))
            .build()

    }
}