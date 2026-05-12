package com.bio_library.user.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.user.domain.model.UserProfileModel;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.UserProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserProfileDtoMapper {
    UserProfileResponse toResponse(UserProfileModel profile);
}
