package com.bio_library.university_mock.domain.model;

import com.bio_library.university_mock.domain.enums.University;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UniversityStudent {
    Long id;
    String carnet;
    String dni;
    String name;
    String lastName;
    String email;
    University university;
    Double gpa;
    Boolean enrolled;
}
