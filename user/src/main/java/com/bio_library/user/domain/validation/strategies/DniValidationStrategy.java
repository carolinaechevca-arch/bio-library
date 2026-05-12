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
@Order(2)
public class DniValidationStrategy implements StudentValidationStrategy {

    @Override
    public void validate(Student student, UniversityStudentData uniData) {
        Optional.of(student.getUser().getDni())
                .filter(dni -> dni.equals(uniData.dni()))
                .orElseThrow(() -> {
                    log.warn("DNI mismatch: request={} university={}",
                            student.getUser().getDni(), uniData.dni());
                    return new UniversityDataMismatchException(DomainConstants.UNIVERSITY_DNI_MISMATCH);
                });
    }
}
