package com.bio_library.user.application.ports.out;

import com.bio_library.user.domain.model.Student;

public interface IStudentPersistencePort {
    Student saveStudent(Student student);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}
