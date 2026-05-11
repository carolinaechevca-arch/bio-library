package com.bio_library.university_mock.infrastructure.adapters.driven.jpa.adapter;

import com.bio_library.university_mock.domain.constants.UniversityConstants;
import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.domain.ports.out.IUniversityStudentPersistencePort;
import com.bio_library.university_mock.infrastructure.adapters.driven.jpa.entity.UniversityStudentEntity;
import com.bio_library.university_mock.infrastructure.adapters.driven.jpa.mapper.IUniversityStudentEntityMapper;
import com.bio_library.university_mock.infrastructure.adapters.driven.jpa.repository.IUniversityStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniversityStudentPersistenceAdapter implements IUniversityStudentPersistencePort {

    private final IUniversityStudentRepository repository;
    private final IUniversityStudentEntityMapper mapper;

    @Override
    public UniversityStudent findByCarnetAndUniversity(String carnet, University university) {
        log.info(UniversityConstants.LOG_FIND_BY_CARNET, carnet, university);
        UniversityStudentEntity entity = repository.findByCarnetAndUniversity(carnet, university);
        return mapper.toDomain(entity);
    }

    @Override
    public List<UniversityStudent> findAllByUniversity(University university) {
        log.info(UniversityConstants.LOG_FIND_ALL_BY_UNIVERSITY, university);
        List<UniversityStudentEntity> entities = repository.findAllByUniversity(university);
        return mapper.toDomainList(entities);
    }
}
