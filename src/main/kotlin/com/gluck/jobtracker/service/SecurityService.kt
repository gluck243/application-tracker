package com.gluck.jobtracker.service

import com.vaadin.flow.spring.security.AuthenticationContext
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class SecurityService(private val context: AuthenticationContext) {

    fun getAuthenticatedUser(): Optional<UserDetails> {
        return context.getAuthenticatedUser(UserDetails::class.java)
    }

    fun logout() {
        context.logout()
    }

}