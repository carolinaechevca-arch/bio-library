package com.bio_library.user.infrastructure.adapters.driven.jpa.mapper;

import com.bio_library.user.domain.model.User;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity entity);
}
