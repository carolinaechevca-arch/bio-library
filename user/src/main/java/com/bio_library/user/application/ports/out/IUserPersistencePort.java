package com.bio_library.user.application.ports.out;

import com.bio_library.user.domain.model.User;

public interface IUserPersistencePort {
    User findByEmail (String email);
}
