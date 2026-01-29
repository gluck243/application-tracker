package com.gluck.jobtracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationRequest {
    @NotBlank
    @NotNull
    @Schema(
        description = "Name of a position",
        example = "Software Developer"
    )
    String position = "";

    @NotBlank
    @NotNull
    @Schema(
        description = "Name of a company",
        example = "ABC Ltd."
    )
    String companyName = "";

    @NotNull
    @Schema(
        description = "Status of an application"
    )
    private Status status = Status.APPLIED;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @NotNull
    @Schema(
        description = "Date of application, must be in past 60 days including today",
        examples = {"15.01.26", "30.12.25"}
    )
    private LocalDate dateApplied;

    @Schema(
        description = "Description of the application in free format"
    )
    private String description = "";

}

