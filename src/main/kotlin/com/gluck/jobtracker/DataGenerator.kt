package com.gluck.jobtracker

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataGenerator {

    @Bean
    fun loadData(repository: ApplicationRepository): CommandLineRunner {
        return CommandLineRunner {
            if (repository.count() == 0L) {
                repository.saveAll(listOf(
                    JobApplication(position = "Junior Backend Engineer", companyName = "Google", status = Status.APPLIED),
                    JobApplication(position = "Kotlin Engineer", companyName = "Local Startup N", status = Status.INTERVIEWING),
                    JobApplication(position = "Spring Boot Pro", companyName = "FInTech Corp", status = Status.WISH_LIST),
                ))
                println("Generated demo data for the job tracker.")
            }
        }
    }

}