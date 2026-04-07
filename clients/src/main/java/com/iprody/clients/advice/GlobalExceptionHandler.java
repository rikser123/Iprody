package com.iprody.clients.advice;

import com.iprody.clients.dto.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
    var errorDto = new ErrorDto();
    errorDto.setTimestamp(Instant.now());
    errorDto.setStatus(HttpStatus.BAD_REQUEST);
    errorDto.setMessage(ex.getMessage());
    errorDto.setError("Invalid request");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> handleNotFoundExceptions(EntityNotFoundException ex) {
    var errorDto = new ErrorDto();
    errorDto.setTimestamp(Instant.now());
    errorDto.setStatus(HttpStatus.NOT_FOUND);
    errorDto.setMessage(ex.getMessage());
    errorDto.setError("Entity not found");

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDto> handleRuntimeExceptions(RuntimeException ex) {
    var errorDto = new ErrorDto();
    errorDto.setTimestamp(Instant.now());
    errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    errorDto.setMessage(ex.getMessage());
    errorDto.setError("Internal server error");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
  }
}
