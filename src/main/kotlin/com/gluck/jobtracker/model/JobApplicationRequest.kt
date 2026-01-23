package com.gluck.jobtracker.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class JobApplicationRequest(
    @NotBlank @NotNull
    @field:Schema(
        description = "Name of a position",
        example = "Software Developer",
        required = true
    )
    var position: String = "",

    @NotBlank @NotNull
    @field:Schema(
        description = "Name of a company",
        example = "ABC Ltd.",
        required = true
    )
    var companyName: String = "",

    @NotNull
    @field:Schema(
        description = "Status of an application",
        required = true
    )
    var status: Status = Status.APPLIED,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy") @NotNull
    @field:Schema(
        description = "Date of application, must be in past 60 days including today",
        examples = ["15.01.26", "30.12.25"],
        required = true
    )
    var dateApplied: LocalDate? = null,

    @field:Schema(
        description = "Description of the application in free format",
        required = false
    )
    var description: String? = ""
    )