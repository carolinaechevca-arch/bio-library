package com.bio_library.user.infrastructure.adapters.driving.rest.controller;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal/students")
@RequiredArgsConstructor
public class StudentInternalController {

    private final IStudentServicePort studentServicePort;

    @GetMapping("/{userId}/email")
    public ResponseEntity<Map<String, String>> getStudentEmail(@PathVariable Long userId) {
        log.info("[INTERNAL] GET contact info for userId={}", userId);
        var student = studentServicePort.getStudent(userId);
        String email = student.getUser().getEmail();
        String phone = student.getUser().getPhoneNumber();
        return ResponseEntity.ok(Map.of(
                "email", email != null ? email : "",
                "phone", phone != null ? phone : ""
        ));
    }
}
