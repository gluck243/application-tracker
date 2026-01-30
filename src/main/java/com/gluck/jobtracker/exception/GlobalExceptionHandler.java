package com.gluck.jobtracker.exception;

import com.gluck.jobtracker.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> info = new HashMap<>();
        var errors = ex.getBindingResult();
        errors.getFieldErrors().forEach(error ->
                info.put(error.getField(), error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid field")
        );
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchJobFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchJobException(NoSuchJobFoundException ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage() != null ? ex.getMessage() : "Unknown error");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}

