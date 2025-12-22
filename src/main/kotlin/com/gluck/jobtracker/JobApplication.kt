package com.gluck.jobtracker

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "job_applications")
class JobApplication(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var position: String = "",

    @Column(nullable = false)
    var companyName: String = "",

    @Enumerated @Column(nullable = false)
    val status: Status = Status.WISH_LIST,

    @Column(nullable = true)
    val dateApplied: LocalDate? = null,

    @Lob @Column(nullable = true)
    val description: String? = ""
) {
    constructor() : this(0)
}