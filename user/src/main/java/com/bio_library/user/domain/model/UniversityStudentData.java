package com.bio_library.user.domain.model;

import lombok.Builder;

@Builder
public record UniversityStudentData(
        String carnet,
        String dni,
        String name,
        String lastName,
        String email,
        String university,
        Double gpa,
        Boolean enrolled
) {}
