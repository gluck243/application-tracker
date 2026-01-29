package com.gluck.jobtracker.repository;

import com.gluck.jobtracker.model.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "job_applications")
public class JobApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    @Column(nullable = false)
    @NotBlank(message = "Position cannot be blank")
    private String position = "";

    @Column(nullable = false)
    @NotBlank(message = "Company name cannot be blank")
    private String companyName = "";

    @Enumerated
    @Column(nullable = false)
    private Status status = Status.APPLIED;

    @Column()
    private LocalDate dateApplied;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description = "";

}

