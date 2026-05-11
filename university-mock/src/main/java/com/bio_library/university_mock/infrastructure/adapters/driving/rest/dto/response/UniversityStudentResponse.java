package com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response;

import com.bio_library.university_mock.domain.enums.University;

public record UniversityStudentResponse(
        String carnet,
        String dni,
        String name,
        String lastName,
        String email,
        University university,
        Double gpa,
        Boolean enrolled
) {}
