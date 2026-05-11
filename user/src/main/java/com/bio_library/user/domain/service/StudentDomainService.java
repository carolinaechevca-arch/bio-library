package com.bio_library.user.domain.service;

import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.exceptions.InvalidUniversityEmailException;
import com.bio_library.user.domain.exceptions.StudentAlreadyExistsException;
import com.bio_library.user.domain.exceptions.StudentNotFoundException;
import com.bio_library.user.domain.exceptions.StudentNotEnrolledException;
import com.bio_library.user.domain.exceptions.UniversityDataMismatchException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class StudentDomainService {

    public Student prepareStudentForRegistration(Student student, String encodedPassword) {
        log.info("Preparing student {} for registration", student.getUser().getEmail());
        return student.toBuilder()
                .user(student.getUser().toBuilder()
                        .password(encodedPassword)
                        .role(Role.STUDENT)
                        .build())
                .hasSanction(false)
                .sanctionEndDate(null)
                .activeLoans(0)
                .build();
    }

    public void validateUniqueness(Student student, boolean emailExists, boolean dniExists) {
        log.info("Validating student uniqueness. emailExists={}, dniExists={}", emailExists, dniExists);

        if (emailExists) {
            throw new StudentAlreadyExistsException(
                    String.format(DomainConstants.STUDENT_EMAIL_ALREADY_EXISTS, student.getUser().getEmail()));
        }
        if (dniExists) {
            throw new StudentAlreadyExistsException(
                    String.format(DomainConstants.STUDENT_DNI_ALREADY_EXISTS, student.getUser().getDni()));
        }
    }

    public Student validateStudentExists(Student student, Long id) {
        return Optional.ofNullable(student).orElseThrow(() -> {
            log.warn("Student with id {} was not found", id);
            return new StudentNotFoundException(String.format(DomainConstants.STUDENT_NOT_FOUND, id));
        });
    }

    public void validateEmailMatchesUniversity(Student student) {
        String email = student.getUser().getEmail();
        String expectedDomain = student.getUser().getUniversity().getEmailDomain();
        if (!email.endsWith("@" + expectedDomain)) {
            log.warn("Email domain mismatch for university {}. Expected @{}, got: {}",
                    student.getUser().getUniversity(), expectedDomain, email);
            throw new InvalidUniversityEmailException(
                    String.format(DomainConstants.INVALID_UNIVERSITY_EMAIL, expectedDomain));
        }
        log.info("Email domain validated for university: {}", student.getUser().getUniversity());
    }

    public void validateUniversityData(Student student, UniversityStudentData uniData) {
        log.info("Validating university data for student: {}", student.getUser().getEmail());
        if (!student.getUser().getDni().equals(uniData.dni())) {
            log.warn("DNI mismatch: request={} university={}", student.getUser().getDni(), uniData.dni());
            throw new UniversityDataMismatchException(DomainConstants.UNIVERSITY_DNI_MISMATCH);
        }
        if (!student.getUser().getEmail().equals(uniData.email())) {
            log.warn("Email mismatch: request={} university={}", student.getUser().getEmail(), uniData.email());
            throw new UniversityDataMismatchException(DomainConstants.UNIVERSITY_EMAIL_MISMATCH);
        }
        if (!Boolean.TRUE.equals(uniData.enrolled())) {
            log.warn("Student not enrolled: {}", student.getUser().getEmail());
            throw new StudentNotEnrolledException(DomainConstants.STUDENT_NOT_ENROLLED);
        }
    }

    public Student applyUniversityData(Student student, UniversityStudentData uniData) {
        log.info("Applying university data (name, lastName, gpa) for: {}", student.getUser().getEmail());
        return student.toBuilder()
                .gpa(uniData.gpa())
                .user(student.getUser().toBuilder()
                        .name(uniData.name())
                        .lastName(uniData.lastName())
                        .build())
                .build();
    }

    public boolean canBorrow(Student student) {
        if (Boolean.TRUE.equals(student.getHasSanction())) {
            return false;
        }
        if (student.getGpa() < 3.2 && student.getActiveLoans() >= 1) {
            return false;
        }
        return true;
    }
}