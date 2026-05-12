package com.bio_library.user.domain.service;

import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.exceptions.InvalidUniversityEmailException;
import com.bio_library.user.domain.exceptions.StudentAlreadyExistsException;
import com.bio_library.user.domain.exceptions.StudentNotFoundException;
import com.bio_library.user.domain.exceptions.StudentNotEnrolledException;
import com.bio_library.user.domain.exceptions.UniversityDataMismatchException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class StudentDomainService {

    public void validateUniqueness(Student student, boolean emailExists, boolean dniExists) {
        log.info("Validating student uniqueness. emailExists={}, dniExists={}", emailExists, dniExists);
        List.of(
                Map.entry(emailExists,
                        String.format(DomainConstants.STUDENT_EMAIL_ALREADY_EXISTS, student.getUser().getEmail())),
                Map.entry(dniExists,
                        String.format(DomainConstants.STUDENT_DNI_ALREADY_EXISTS, student.getUser().getDni()))
        ).stream()
                .filter(Map.Entry::getKey)
                .findFirst()
                .ifPresent(entry -> { throw new StudentAlreadyExistsException(entry.getValue()); });
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
        Optional.of(email)
                .filter(e -> e.endsWith("@" + expectedDomain))
                .orElseThrow(() -> {
                    log.warn("Email domain mismatch for university {}. Expected @{}, got: {}",
                            student.getUser().getUniversity(), expectedDomain, email);
                    return new InvalidUniversityEmailException(
                            String.format(DomainConstants.INVALID_UNIVERSITY_EMAIL, expectedDomain));
                });
        log.info("Email domain validated for university: {}", student.getUser().getUniversity());
    }

    public void validateUniversityData(Student student, UniversityStudentData uniData) {
        log.info("Validating university data for student: {}", student.getUser().getEmail());
        List.<Map.Entry<Boolean, Supplier<RuntimeException>>>of(
                Map.entry(!student.getUser().getDni().equals(uniData.dni()),
                        () -> {
                            log.warn("DNI mismatch: request={} university={}", student.getUser().getDni(), uniData.dni());
                            return new UniversityDataMismatchException(DomainConstants.UNIVERSITY_DNI_MISMATCH);
                        }),
                Map.entry(!student.getUser().getEmail().equals(uniData.email()),
                        () -> {
                            log.warn("Email mismatch: request={} university={}", student.getUser().getEmail(), uniData.email());
                            return new UniversityDataMismatchException(DomainConstants.UNIVERSITY_EMAIL_MISMATCH);
                        }),
                Map.entry(!Boolean.TRUE.equals(uniData.enrolled()),
                        () -> {
                            log.warn("Student not enrolled: {}", student.getUser().getEmail());
                            return new StudentNotEnrolledException(DomainConstants.STUDENT_NOT_ENROLLED);
                        })
        ).stream()
                .filter(Map.Entry::getKey)
                .findFirst()
                .ifPresent(entry -> { throw entry.getValue().get(); });
    }

    public boolean canBorrow(Student student) {
        return !Boolean.TRUE.equals(student.getHasSanction())
                && !(student.getGpa() < 3.2 && student.getActiveLoans() >= 1);
    }
}
