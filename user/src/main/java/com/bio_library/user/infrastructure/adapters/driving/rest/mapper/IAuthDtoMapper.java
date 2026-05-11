package com.bio_library.user.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.user.domain.model.AuthResponseModel;
import com.bio_library.user.domain.model.AuthenticationModel;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.AuthRequestDto;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.AuthResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthDtoMapper {
    AuthenticationModel toModel(AuthRequestDto authRequestDto);

    AuthResponseDto toAuthResponseDto(AuthResponseModel authResponseModel);
}