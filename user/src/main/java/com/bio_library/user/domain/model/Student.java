package com.bio_library.user.domain.model;

import com.bio_library.user.domain.enums.University;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Student {
    User user;
    String carnet;
    Double gpa;
    Boolean hasSanction;
    LocalDateTime sanctionEndDate;
    Integer activeLoans;
}