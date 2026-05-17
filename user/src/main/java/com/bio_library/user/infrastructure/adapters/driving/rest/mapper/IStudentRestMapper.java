package com.bio_library.user.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.StudentRequest;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IStudentRestMapper {

    @Mapping(target = "carnet", source = "carnet")
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.dni", source = "dni")
    @Mapping(target = "user.name", ignore = true)
    @Mapping(target = "user.lastName", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.phoneNumber", source = "phoneNumber")
    @Mapping(target = "user.role", constant = "STUDENT")
    @Mapping(target = "user.university", source = "university")
    @Mapping(target = "gpa", ignore = true)
    @Mapping(target = "hasSanction", ignore = true)
    @Mapping(target = "sanctionEndDate", ignore = true)
    @Mapping(target = "activeLoans", ignore = true)
    Student toStudent(StudentRequest request);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "carnet", source = "carnet")
    @Mapping(target = "dni", source = "user.dni")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "university", source = "user.university")
    @Mapping(target = "gpa", source = "gpa")
    @Mapping(target = "hasSanction", source = "hasSanction")
    @Mapping(target = "sanctionEndDate", source = "sanctionEndDate")
    @Mapping(target = "activeLoans", source = "activeLoans")
    StudentResponse toResponse(Student student);

    default List<StudentResponse> toResponseList(List<Student> students) {
        return students.stream()
                .map(this::toResponse)
                .toList();
    }

}