package com.bio_library.user.infrastructure.adapters.driven.feign.mapper;

import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.infrastructure.adapters.driven.feign.dto.UniversityStudentFeignResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUniversityFeignMapper {
    UniversityStudentData toDomain(UniversityStudentFeignResponse response);
}
