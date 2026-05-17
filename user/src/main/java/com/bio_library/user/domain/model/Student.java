package com.bio_library.user.domain.model;

import com.bio_library.user.domain.enums.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Student {
    User user;
    String carnet;
    Double gpa;
    Boolean hasSanction;
    LocalDate sanctionEndDate;
    Integer activeLoans;

    public Student withRegistrationDefaults(String encodedPassword) {
        return toBuilder()
                .user(user.toBuilder()
                        .password(encodedPassword)
                        .role(Role.STUDENT)
                        .build())
                .hasSanction(false)
                .sanctionEndDate(null)
                .activeLoans(0)
                .build();
    }

    public Student withUniversityData(UniversityStudentData uniData) {
        return toBuilder()
                .gpa(uniData.gpa())
                .user(user.toBuilder()
                        .name(uniData.name())
                        .lastName(uniData.lastName())
                        .build())
                .build();
    }

    public Student withSanction(Boolean active, LocalDate sanctionEndDate) {
        return toBuilder()
                .hasSanction(active)
                .sanctionEndDate(Boolean.TRUE.equals(active) ? sanctionEndDate : null)
                .build();
    }
}
