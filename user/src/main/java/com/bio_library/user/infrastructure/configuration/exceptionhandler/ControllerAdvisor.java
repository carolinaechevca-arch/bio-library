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
    public ResponseEntity<ExceptionResponse> handlerInvalidCredentialsException(InvalidCredentialsException ex) {
        return response(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerArgumentInvalidException(MethodArgumentNotValidException ex) {
        FieldError firstFieldError = ex.getFieldErrors().get(0);
        return response(firstFieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handlerStudentAlreadyExistsException(StudentAlreadyExistsException ex) {
        return response(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerStudentNotFoundException(StudentNotFoundException ex) {
        return response(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUniversityEmailException.class)
    public ResponseEntity<ExceptionResponse> handlerInvalidUniversityEmailException(InvalidUniversityEmailException ex) {
        return response(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return response(ACCESS_DENIED_EXCEPTION_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex) {
        return response(AUTHENTICATION_REQUIRED_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UniversityStudentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUniversityStudentNotFoundException(UniversityStudentNotFoundException ex) {
        return response(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UniversityDataMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleUniversityDataMismatchException(UniversityDataMismatchException ex) {
        return response(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<ExceptionResponse> handleStudentNotEnrolledException(StudentNotEnrolledException ex) {
        return response(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static ResponseEntity<ExceptionResponse> response(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ExceptionResponse.builder()
                        .message(message)
                        .status(status.getReasonPhrase())
                        .timestamp(LocalDateTime.now())
                        .code(status.value())
                        .build()
        );
    }
}
