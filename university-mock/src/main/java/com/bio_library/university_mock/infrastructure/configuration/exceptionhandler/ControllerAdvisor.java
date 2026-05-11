package com.bio_library.university_mock.infrastructure.configuration.exceptionhandler;

import com.bio_library.university_mock.domain.exceptions.StudentNotFoundException;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleStudentNotFoundException(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value()));
    }
}
