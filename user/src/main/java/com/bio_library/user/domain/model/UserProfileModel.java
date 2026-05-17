package com.bio_library.user.domain.model;

import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.enums.University;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserProfileModel {
    Long id;
    String dni;
    String name;
    String lastName;
    String email;
    String phoneNumber;
    Role role;
    University university;
    String carnet;
    Double gpa;
    Boolean hasSanction;
    LocalDate sanctionEndDate;
    Integer activeLoans;

    public static UserProfileModel from(Student student) {
        User user = student.getUser();
        return UserProfileModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .university(user.getUniversity())
                .carnet(student.getCarnet())
                .gpa(student.getGpa())
                .hasSanction(student.getHasSanction())
                .sanctionEndDate(student.getSanctionEndDate())
                .activeLoans(student.getActiveLoans())
                .build();
    }

    public static UserProfileModel from(User user) {
        return UserProfileModel.builder()
                .id(user.getId())
                .dni(user.getDni())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .university(user.getUniversity())
                .build();
    }
}
