package com.bio_library.user.infrastructure.adapters.driving.rest.controller;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.StudentRequest;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.StudentResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.mapper.IStudentRestMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student Management")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final IStudentServicePort studentServicePort;
    private final IStudentRestMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        Student saved = studentServicePort.createStudent(mapper.toStudent(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

}