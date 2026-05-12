package com.bio_library.user.domain.validation.strategies;

import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.exceptions.UniversityDataMismatchException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.domain.validation.StudentValidationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(3)
public class EmailMatchValidationStrategy implements StudentValidationStrategy {

    @Override
    public void validate(Student student, UniversityStudentData uniData) {
        Optional.of(student.getUser().getEmail())
                .filter(email -> email.equals(uniData.email()))
                .orElseThrow(() -> {
                    log.warn("Email mismatch: request={} university={}",
                            student.getUser().getEmail(), uniData.email());
                    return new UniversityDataMismatchException(DomainConstants.UNIVERSITY_EMAIL_MISMATCH);
                });
    }
}
