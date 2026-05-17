package com.bio_library.user.infrastructure.adapters.driving.rest.controller;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.SanctionRequestDto;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.StudentRequest;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.StudentResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.mapper.IStudentRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @ApiResponse(responseCode = "201", description = "Student created successfully",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or email does not match the university domain",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions (ADMIN role required)",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "409", description = "A student with the same email or DNI already exists",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "422", description = "Student not found in the university system, data mismatch, or student is not enrolled",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student saved = studentServicePort.createStudent(mapper.toStudent(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @Operation(
            summary = "List students",
            description = "Returns a paginated list of students belonging to the admin's university (resolved from JWT). Requires ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated list of students"),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<StudentResponse>> getStudents(
            Authentication authentication,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by (carnet, gpa, user.name, user.lastName)", example = "carnet")
            @RequestParam(defaultValue = "carnet") String sortBy,
            @Parameter(description = "Sort direction: asc or desc", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("[LIST] GET /students adminEmail={} page={} size={} sortBy={} sortDir={}",
                authentication.getName(), page, size, sortBy, sortDir);

        return ResponseEntity.ok(studentServicePort.getStudents(authentication.getName(), pageable).map(mapper::toResponse));
    }

    @Operation(
            summary = "Get student detail",
            description = "Returns the full profile of a student by their user ID. Requires ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        log.info("[GET] GET /students/{}", id);
        return ResponseEntity.ok(mapper.toResponse(studentServicePort.getStudent(id)));
    }

    @Operation(
            summary = "Apply or lift a sanction",
            description = "Sets or removes a sanction on a student. " +
                    "When active=true a sanctionEndDate is recommended. " +
                    "When active=false the sanction is lifted and sanctionEndDate is cleared. Requires ADMIN role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sanction updated",
                    content = @Content(schema = @Schema(implementation = StudentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}/sanction")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> updateSanction(
            @PathVariable Long id,
            @Valid @RequestBody SanctionRequestDto request) {
        log.info("[SANCTION] PATCH /students/{}/sanction active={}", id, request.active());
        Student updated = studentServicePort.updateSanction(id, request.active(), request.sanctionEndDate());

        return ResponseEntity.ok(mapper.toResponse(updated));
    }
}
