package com.jrpolesi.ice_cream_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException exception) {
    Map<String, String> errors = new HashMap<>();
    exception.getConstraintViolations().forEach(
        violation -> errors.put(
            violation.getPropertyPath().toString(),
            violation.getMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            Map.of(
                "error", "An unexpected error occurred",
                "message", exception.getMessage()));
  }
}
