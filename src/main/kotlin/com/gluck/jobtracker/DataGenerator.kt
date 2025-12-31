package com.gluck.jobtracker

import com.gluck.jobtracker.model.JobApplicationEntity
import com.gluck.jobtracker.model.Status
import com.gluck.jobtracker.repository.ApplicationRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

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
                            description = "[link], position in Helsinki, credible company"
                        ),
                        JobApplicationEntity(
                            position = "Kotlin Engineer",
                            companyName = "Local Startup N",
                            status = Status.INTERVIEWING,
                            dateApplied = LocalDate.of(2025, 12, 1),
                            description = "gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
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

}