package com.bio_library.user.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {
}
