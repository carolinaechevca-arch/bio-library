package com.bio_library.catalog.infrastructure.configuration.exceptionhandler;

import com.bio_library.catalog.domain.exceptions.BookNotFoundException;
import com.bio_library.catalog.domain.exceptions.LoanLimitExceededException;
import com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(LoanLimitExceededException.class)
    public ResponseEntity<ExceptionResponse> handleLoanLimitExceededException(LoanLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                message, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value()));
    }
}
