package com.gluck.jobtracker

import com.gluck.jobtracker.repository.UserRepository
import com.gluck.jobtracker.service.UserDetailsServiceImpl
import com.gluck.jobtracker.ui.LoginView
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer.vaadin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.csrf { it.disable() }

        http.authorizeHttpRequests { c ->
            c.requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            c.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
            c.requestMatchers("/api/**").hasRole("ADMIN")
            c.requestMatchers("/**").permitAll()
        }

        http.with(vaadin()) { v ->
            v.loginView(LoginView::class.java)
        }

        return http.build()

    }

}