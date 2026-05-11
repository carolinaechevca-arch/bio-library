package com.bio_library.university_mock.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.infrastructure.adapters.driven.jpa.entity.UniversityStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUniversityStudentRepository extends JpaRepository<UniversityStudentEntity, Long> {
    UniversityStudentEntity findByCarnetAndUniversity(String carnet, University university);

    List<UniversityStudentEntity> findAllByUniversity(University university);
}
