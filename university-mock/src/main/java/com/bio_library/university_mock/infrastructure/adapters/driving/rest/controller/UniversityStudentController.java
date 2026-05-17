package com.bio_library.university_mock.infrastructure.adapters.driving.rest.controller;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.application.ports.in.IUniversityStudentServicePort;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response.UniversityStudentResponse;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.mapper.IUniversityStudentRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/university/students")
@RequiredArgsConstructor
@Tag(name = "University Students", description = "University system read-only queries")
public class UniversityStudentController {

    private final IUniversityStudentServicePort servicePort;
    private final IUniversityStudentRestMapper restMapper;

    @Operation(
            summary = "Get student by carnet and university",
            description = "Returns the academic profile of a student given their carnet and university. " +
                    "Includes enrollment status and GPA."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Student found",
                    content = @Content(schema = @Schema(implementation = UniversityStudentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No student found with the given carnet",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/{carnet}/{university}")
    public ResponseEntity<UniversityStudentResponse> getStudent(
            @Parameter(description = "Student registration number", example = "20210001")
            @PathVariable String carnet,
            @Parameter(description = "University identifier (enum value)", example = "ITM")
            @PathVariable University university) {
        log.info("[REST] GET student carnet={} university={}", carnet, university);
        UniversityStudent student = servicePort.getStudentByCarnetAndUniversity(carnet, university);
        log.info("[REST] Student found: {}", student);
        return ResponseEntity.ok(restMapper.toResponse(student));
    }

    @Operation(
            summary = "Get all students by university",
            description = "Returns all registered students for a given university. Returns an empty list if none exist."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of students (may be empty)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UniversityStudentResponse.class)))
            )
    })
    @GetMapping("/university/{university}")
    public ResponseEntity<List<UniversityStudentResponse>> getAllStudents(
            @Parameter(description = "University identifier (enum value)", example = "ITM")
            @PathVariable University university) {
        log.info("[REST] GET all students for university={}", university);
        List<UniversityStudent> students = servicePort.getAllStudentsByUniversity(university);
        log.info("[REST] Students found: {}", students.size());
        return ResponseEntity.ok(restMapper.toResponseList(students));
    }
}
