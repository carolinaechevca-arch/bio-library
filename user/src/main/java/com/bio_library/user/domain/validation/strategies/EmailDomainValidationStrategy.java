package com.bio_library.user.domain.validation.strategies;

import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.exceptions.InvalidUniversityEmailException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.domain.validation.StudentValidationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(1)
public class EmailDomainValidationStrategy implements StudentValidationStrategy {

    @Override
    public void validate(Student student, UniversityStudentData uniData) {
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
}
