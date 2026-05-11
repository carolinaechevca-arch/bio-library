package com.bio_library.university_mock.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.infrastructure.adapters.driving.rest.dto.response.UniversityStudentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUniversityStudentRestMapper {
    UniversityStudentResponse toResponse(UniversityStudent student);
    List<UniversityStudentResponse> toResponseList(List<UniversityStudent> students);
}
