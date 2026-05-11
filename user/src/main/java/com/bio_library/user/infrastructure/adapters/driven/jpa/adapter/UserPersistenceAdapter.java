package com.bio_library.user.infrastructure.adapters.driven.jpa.adapter;

import com.bio_library.user.application.ports.out.IUserPersistencePort;
import com.bio_library.user.domain.model.User;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.UserEntity;
import com.bio_library.user.infrastructure.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.bio_library.user.infrastructure.adapters.driven.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bio_library.user.infrastructure.adapters.driven.util.PersistenceConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public User findByEmail(String email) {
        log.info(USER_FIND_BY_EMAIL, email);
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            log.warn(USER_NOT_FOUND, email);
            return null;
        }
        log.info(USER_FOUND, email);
        return userEntityMapper.toDomain(entity);
    }
}
