package com.gluck.jobtracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record JobApplicationResponse(
    @Schema(
        description = "Identification number",
        example = "3"
    )
    Long id,
    @Schema(
        description = "Name of a position",
        example = "Software Developer"
    )
    String position,
    @Schema(
        description = "Name of a company",
        example = "ABC Ltd."
    )
    String companyName,
    @Schema(
        description = "Status of an application"
    )
    Status status, @Schema(
        description = "Date of application, must be in past 60 days including today",
        examples = {"15.01.26", "30.12.25"}
    )
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd.MM.yyyy"
    )
    LocalDate dateApplied,
    @Schema(
        description = "Description of the application in free format"
    )
    String description) {

}

