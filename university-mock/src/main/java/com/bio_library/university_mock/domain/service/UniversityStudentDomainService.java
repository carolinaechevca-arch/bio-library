package com.bio_library.university_mock.domain.service;

import com.bio_library.university_mock.domain.constants.UniversityConstants;
import com.bio_library.university_mock.domain.exceptions.StudentNotFoundException;
import com.bio_library.university_mock.domain.model.UniversityStudent;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class UniversityStudentDomainService {

    public UniversityStudent validateStudentExists(UniversityStudent student, String carnet) {
        return Optional.ofNullable(student).orElseThrow(() -> {
            log.warn(UniversityConstants.LOG_STUDENT_NOT_FOUND, carnet);
            return new StudentNotFoundException(String.format(UniversityConstants.STUDENT_NOT_FOUND, carnet));
        });
    }
}
