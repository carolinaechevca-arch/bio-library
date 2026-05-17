package com.bio_library.user.infrastructure.adapters.driven.jpa.repository;

import com.bio_library.user.domain.enums.University;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByUser_Id(Long userId);
    Page<StudentEntity> findByUser_University(University university, Pageable pageable);
    List<StudentEntity> findByHasSanctionTrueAndSanctionEndDateBefore(LocalDate today);
}
