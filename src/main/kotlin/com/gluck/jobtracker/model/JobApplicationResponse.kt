package com.gluck.jobtracker.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class JobApplicationResponse(

    @field:Schema(
        description = "Identification number",
        example = "3"
    )
    val id: Long,

    @field:Schema(
        description = "Name of a position",
        example = "Software Developer",
    )
    val position: String,

    @field:Schema(
        description = "Name of a company",
        example = "ABC Ltd.",
    )
    val companyName: String,

    @field:Schema(
        description = "Status of an application",
        examples = ["WISH_LIST", "APPLIED", "GHOSTED", "REJECTED", "INTERVIEWING", "OFFER"],
    )
    val status: Status,

    @field:Schema(
        description = "Date of application, must be in past 60 days including today",
        examples = ["15.01.26", "30.12.25"],
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    val dateApplied: LocalDate?,

    @field:Schema(
        description = "Description of the application in free format",
        required = false
    )
    val description: String?

){}