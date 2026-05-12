package com.bio_library.user.domain.validation.strategies;

import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.exceptions.StudentNotEnrolledException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.domain.validation.StudentValidationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Order(4)
public class EnrollmentValidationStrategy implements StudentValidationStrategy {

    @Override
    public void validate(Student student, UniversityStudentData uniData) {
        Optional.ofNullable(uniData.enrolled())
                .filter(Boolean.TRUE::equals)
                .orElseThrow(() -> {
                    log.warn("Student not enrolled: {}", student.getUser().getEmail());
                    return new StudentNotEnrolledException(DomainConstants.STUDENT_NOT_ENROLLED);
                });
    }
}
