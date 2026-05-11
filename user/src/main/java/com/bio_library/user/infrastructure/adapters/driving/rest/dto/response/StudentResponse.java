package com.bio_library.user.infrastructure.adapters.driving.rest.dto.response;


import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.enums.University;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String carnet;
    private String dni, name, lastName, email, phoneNumber;
    private Role role;
    private University university;
    private Double gpa;
    private Boolean hasSanction;
    private LocalDateTime sanctionEndDate;
    private Integer activeLoans;
}