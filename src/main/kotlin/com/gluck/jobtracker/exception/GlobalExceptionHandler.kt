package com.gluck.jobtracker.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {

        val info = mutableMapOf<String, String>()
        val errors = ex.bindingResult
        for (error in errors.fieldErrors) {
            info[error.field] = error.defaultMessage ?: "Invalid field"
        }
        return ResponseEntity(info, HttpStatus.BAD_REQUEST)

    }

}