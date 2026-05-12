package com.bio_library.user.application.ports.out;

import com.bio_library.user.domain.enums.University;
import com.bio_library.user.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IStudentPersistencePort {
    Student saveStudent(Student student);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Double findGpaByUserId(Long userId);
    Optional<Student> findByUserId(Long userId);
    Page<Student> findAll(University university, Pageable pageable);
    Student updateStudent(Student student);
}
