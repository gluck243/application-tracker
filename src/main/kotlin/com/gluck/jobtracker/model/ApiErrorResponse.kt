package com.gluck.jobtracker.model

import io.swagger.v3.oas.annotations.media.Schema

class ApiErrorResponse(
    @field:Schema(
        description = "Error message",
        example = "No such Job found",
        required = false
    )
    var error: String?
)