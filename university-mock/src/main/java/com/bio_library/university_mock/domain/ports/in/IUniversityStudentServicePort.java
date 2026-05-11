package com.bio_library.university_mock.domain.ports.in;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;

import java.util.List;

public interface IUniversityStudentServicePort {
    UniversityStudent getStudentByCarnetAndUniversity(String carnet, University university);
    List<UniversityStudent> getAllStudentsByUniversity(University university);
}
