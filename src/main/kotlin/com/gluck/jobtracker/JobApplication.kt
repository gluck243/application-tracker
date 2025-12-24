package com.gluck.jobtracker

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
@Table(name = "job_applications")
class JobApplication(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false) @NotBlank(message = "Position cannot be blank")
    var position: String = "",

    @Column(nullable = false) @NotBlank(message = "Company name cannot be blank")
    var companyName: String = "",

    @Enumerated @Column(nullable = false)
    var status: Status = Status.WISH_LIST,

    @Column(nullable = true)
    val dateApplied: LocalDate? = null,

    @Lob @Column(nullable = true)
    val description: String? = ""
) {}