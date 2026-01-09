package com.gluck.jobtracker

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.model.UserEntity
import com.gluck.jobtracker.repository.ApplicationRepository
import com.gluck.jobtracker.repository.UserRepository
import com.vaadin.copilot.shaded.bouncycastle.crypto.generators.BCrypt
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import kotlin.uuid.Uuid

@Configuration
class DataGenerator {

    @Bean
    fun loadData(repository: ApplicationRepository): CommandLineRunner {
        return CommandLineRunner {
            if (repository.count() == 0L) {
                repository.saveAll(
                    listOf(
                        JobApplicationEntity(
                            position = "Junior Backend Engineer",
                            companyName = "Google",
                            status = Status.APPLIED,
                            dateApplied = LocalDate.of(2025, 12, 17),
                            description = "TEST DATA, [link], position in Helsinki, credible company"
                        ),
                        JobApplicationEntity(
                            position = "Kotlin Engineer",
                            companyName = "Local Startup N",
                            status = Status.INTERVIEWING,
                            dateApplied = LocalDate.of(2025, 12, 1),
                            description = "TEST DATA, gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        ),
                        JobApplicationEntity(
                            position = "Spring Boot Pro",
                            companyName = "FInTech Corp",
                            status = Status.WISH_LIST,
                            dateApplied = LocalDate.of(2025, 12, 7)
                        ),
                    )
                )
                println("Generated demo data for the job tracker.")
            }
        }
    }

    @Bean
    fun loadUserData(repository: UserRepository, passwordEncoder: PasswordEncoder): CommandLineRunner {
        return CommandLineRunner {
            if (repository.findByUsername("admin") == null) {
                repository.save(
                    UserEntity(
                        username = "admin",
                        password = passwordEncoder.encode("superPassword")!!, // I know it shouldn't be here
                        role = "ADMIN"                                                      // I am working on keeping it a secret
                    )
                )
                println("Generated ADMIN user.")
            }
        }
    }

}