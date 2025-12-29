package com.gluck.jobtracker.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class JobApplicationRequest(
    @NotBlank @NotNull
    var position: String = "",
    @NotNull @NotNull
    var companyName: String = "",
    @NotNull
    var status: Status = Status.WISH_LIST,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy") @NotNull
    var dateApplied: LocalDate? = null,
    var description: String? = ""
    )