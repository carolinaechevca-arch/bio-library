package com.bio_library.university_mock.infrastructure.adapters.driven.jpa.entity;

import com.bio_library.university_mock.domain.enums.University;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "university_students")
@Getter
@Setter
public class UniversityStudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String carnet;

    @Column(unique = true, nullable = false, length = 20)
    private String dni;

    private String name;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private University university;

    private Double gpa;
    private Boolean enrolled;
}
