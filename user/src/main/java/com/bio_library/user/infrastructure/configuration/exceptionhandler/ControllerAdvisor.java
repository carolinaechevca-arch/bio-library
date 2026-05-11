package com.bio_library.user.infrastructure.configuration.exceptionhandler;

import com.bio_library.user.domain.exceptions.InvalidCredentialsException;
import com.bio_library.user.domain.exceptions.InvalidUniversityEmailException;
import com.bio_library.user.domain.exceptions.StudentAlreadyExistsException;
import com.bio_library.user.domain.exceptions.StudentNotFoundException;
import com.bio_library.user.domain.exceptions.StudentNotEnrolledException;
import com.bio_library.user.domain.exceptions.UniversityDataMismatchException;
import com.bio_library.user.domain.exceptions.UniversityStudentNotFoundException;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import java.time.LocalDateTime;

import static com.bio_library.user.domain.constants.AuthConstants.ACCESS_DENIED_EXCEPTION_MESSAGE;
import static com.bio_library.user.domain.constants.AuthConstants.AUTHENTICATION_REQUIRED_MESSAGE;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handlerInvalidCredentialsException(
            InvalidCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerArgumentInvalidException(
            MethodArgumentNotValidException exception) {
        FieldError firstFieldError = exception.getFieldErrors().get(0);
        return ResponseEntity.badRequest().body(new ExceptionResponse(firstFieldError.getDefaultMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handlerStudentAlreadyExistsException(
            StudentAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.CONFLICT.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerStudentNotFoundException(
            StudentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(InvalidUniversityEmailException.class)
    public ResponseEntity<ExceptionResponse> handlerInvalidUniversityEmailException(
            InvalidUniversityEmailException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(
                ACCESS_DENIED_EXCEPTION_MESSAGE, HttpStatus.FORBIDDEN.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                AUTHENTICATION_REQUIRED_MESSAGE, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(UniversityStudentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUniversityStudentNotFoundException(
            UniversityStudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @ExceptionHandler(UniversityDataMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleUniversityDataMismatchException(
            UniversityDataMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<ExceptionResponse> handleStudentNotEnrolledException(
            StudentNotEnrolledException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }
}
