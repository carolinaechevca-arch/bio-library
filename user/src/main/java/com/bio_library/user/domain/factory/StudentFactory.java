package com.bio_library.user.domain.factory;

import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;

public class StudentFactory {

    private StudentFactory() {}

    public static Student create(Student student, UniversityStudentData uniData, String encodedPassword) {
        return student
                .withUniversityData(uniData)
                .withRegistrationDefaults(encodedPassword);
    }
}
