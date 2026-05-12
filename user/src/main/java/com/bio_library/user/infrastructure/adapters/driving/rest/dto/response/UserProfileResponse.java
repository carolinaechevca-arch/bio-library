package com.bio_library.user.infrastructure.adapters.driving.rest.dto.response;

import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.enums.University;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserProfileResponse(
        Long id,
        String dni,
        String name,
        String lastName,
        String email,
        String phoneNumber,
        Role role,
        University university,
        String carnet,
        Double gpa,
        Boolean hasSanction,
        LocalDateTime sanctionEndDate,
        Integer activeLoans
) {}
