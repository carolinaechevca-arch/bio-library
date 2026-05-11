package com.bio_library.university_mock.infrastructure.adapters.driven.jpa.mapper;

import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.infrastructure.adapters.driven.jpa.entity.UniversityStudentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUniversityStudentEntityMapper {
    UniversityStudent toDomain(UniversityStudentEntity entity);
    List<UniversityStudent> toDomainList(List<UniversityStudentEntity> entities);
}

