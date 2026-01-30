package com.gluck.jobtracker;

import com.gluck.jobtracker.repository.JobApplicationEntity;
import com.gluck.jobtracker.model.Status;
import com.gluck.jobtracker.repository.UserEntity;
import com.gluck.jobtracker.repository.ApplicationRepository;
import com.gluck.jobtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataGenerator {

    @Value("${admin.username}")
    private String adminUser;

    @Value("${admin.password}")
    private String adminPass;

    @Bean
    public CommandLineRunner loadData(ApplicationRepository repository) {
        return args -> {
            if (repository.count() == 0L) {
                repository.saveAll(
                    List.of(
                        createJob(
                            "Junior Backend Engineer",
                            "Google",
                            Status.APPLIED,
                            LocalDate.of(2025, 12, 17),
                            "TEST DATA, [link], position in Helsinki, credible company"
                        ),
                        createJob(
                            "Kotlin Engineer",
                            "Local Startup N",
                            Status.INTERVIEWING,
                            LocalDate.of(2025, 12, 1),
                            "TEST DATA, gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        ),
                        createJob(
                            "Spring Boot Pro",
                            "FInTech Corp",
                            Status.WISH_LIST,
                            LocalDate.of(2025, 12, 7),
                            null
                        )
                    )
                );
                System.out.println("Generated demo data for the job tracker.");
            }
        };
    }

    private JobApplicationEntity createJob(
        String position,
        String company,
        Status status,
        LocalDate dateApplied,
        String description
    ) {
        JobApplicationEntity entity = new JobApplicationEntity();
        entity.setPosition(position);
        entity.setCompanyName(company);
        entity.setStatus(status);
        entity.setDateApplied(dateApplied);
        entity.setDescription(description);
        return entity;
    }

    @Bean
    public CommandLineRunner loadUserData(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.findByUsername(adminUser) == null) {
                UserEntity user = new UserEntity(
                        adminUser,
                        passwordEncoder.encode(adminPass),
                        "ADMIN"
                );
                repository.save(user);
                System.out.println("Generated ADMIN user.");
            } else {
                System.out.println("User " + adminUser + " already generated. Skipping");
            }
        };
    }
}

