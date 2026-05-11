package com.bio_library.university_mock.domain.ports.out;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;

import java.util.List;

public interface IUniversityStudentPersistencePort {
    UniversityStudent findByCarnetAndUniversity(String carnet, University university);
    List<UniversityStudent> findAllByUniversity(University university);
}
