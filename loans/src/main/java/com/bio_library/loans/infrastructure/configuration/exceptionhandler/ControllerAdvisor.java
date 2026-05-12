package com.bio_library.loans.infrastructure.configuration.exceptionhandler;

import com.bio_library.loans.domain.exceptions.BookNotAvailableException;
import com.bio_library.loans.domain.exceptions.BookNotFoundException;
import com.bio_library.loans.domain.exceptions.LoanBlockedException;
import com.bio_library.loans.domain.exceptions.LoanNotActiveException;
import com.bio_library.loans.domain.exceptions.LoanNotFoundException;
import com.bio_library.loans.domain.exceptions.LoanOwnershipException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleLoanNotFoundException(LoanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBookNotFoundException(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ExceptionResponse> handleBookNotAvailableException(BookNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(LoanBlockedException.class)
    public ResponseEntity<ExceptionResponse> handleLoanBlockedException(LoanBlockedException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(LoanNotActiveException.class)
    public ResponseEntity<ExceptionResponse> handleLoanNotActiveException(LoanNotActiveException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(LoanOwnershipException.class)
    public ResponseEntity<ExceptionResponse> handleLoanOwnershipException(LoanOwnershipException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.FORBIDDEN.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.FORBIDDEN.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.name())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
