package com.bio_library.university_mock.infrastructure.adapters.driving.rest.controller;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.domain.ports.in.IUniversityStudentServicePort;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response.UniversityStudentResponse;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.mapper.IUniversityStudentRestMapper;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get student by carnet and university")
    @GetMapping("/{carnet}/{university}")
    public ResponseEntity<UniversityStudentResponse> getStudent(
            @PathVariable String carnet,
            @PathVariable University university) {
        log.info("[REST] GET student carnet={} university={}", carnet, university);
        UniversityStudent student = servicePort.getStudentByCarnetAndUniversity(carnet, university);
        log.info("[REST] Student found: {}", student);
        return ResponseEntity.ok(restMapper.toResponse(student));
    }

    @GetMapping("/university/{university}")
    public ResponseEntity<List<UniversityStudentResponse>> getAllStudents(@PathVariable University university) {
        log.info("[REST] GET all students for university={}", university);
        List<UniversityStudent> students = servicePort.getAllStudentsByUniversity(university);
        log.info("[REST] Students found: {}", students.size());
        return ResponseEntity.ok(restMapper.toResponseList(students));
    }


}
