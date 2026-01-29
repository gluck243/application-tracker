package com.gluck.jobtracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiErrorResponse {

    @Schema(
        description = "Error message",
        example = "No such Job found"
    )
    private String error;

}

