package com.bio_library.user.infrastructure.adapters.driven.jpa.mapper;

import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IUserEntityMapper.class})
public interface IStudentEntityMapper {

    @Mapping(target = "id", ignore = true)
    StudentEntity toEntity(Student student);

    Student toDomain(StudentEntity entity);
}
