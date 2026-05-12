package com.bio_library.user.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByUser_Id(Long userId);
}
