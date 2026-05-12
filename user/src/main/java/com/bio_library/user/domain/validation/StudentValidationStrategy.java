package com.bio_library.user.domain.validation;

import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;

public interface StudentValidationStrategy {
    void validate(Student student, UniversityStudentData uniData);
}
