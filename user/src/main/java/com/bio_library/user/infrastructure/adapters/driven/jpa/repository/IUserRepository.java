package com.bio_library.user.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    UserEntity findByEmail(String email);
}