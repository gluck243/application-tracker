package com.gluck.jobtracker.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

class JobApplicationResponse(
    val id: Long,
    val position: String,
    val companyName: String,
    val status: Status,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    val dateApplied: LocalDate?,
    val description: String?
){}