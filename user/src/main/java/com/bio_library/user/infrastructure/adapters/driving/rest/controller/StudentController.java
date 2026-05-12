package com.bio_library.user.infrastructure.adapters.driving.rest.controller;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.StudentRequest;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.StudentResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.mapper.IStudentRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Management", description = "Student registration endpoints. Requires ADMIN role.")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final IStudentServicePort studentServicePort;
    private final IStudentRestMapper mapper;

    @Operation(
            summary = "Register a new student",
            description = "Creates a student account after validating their existence and enrollment status " +
                    "against the university system. Requires a valid JWT with ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error or email does not match the university domain",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Insufficient permissions (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A student with the same email or DNI already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Student not found in the university system, data mismatch, or student is not enrolled",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @PostMapping("/create")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student saved = studentServicePort.createStudent(mapper.toStudent(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

}