package com.bio_library.user.infrastructure.adapters.driven.feign.dto;

public record UniversityStudentFeignResponse(
        String carnet,
        String dni,
        String name,
        String lastName,
        String email,
        String university,
        Double gpa,
        Boolean enrolled
) {}
