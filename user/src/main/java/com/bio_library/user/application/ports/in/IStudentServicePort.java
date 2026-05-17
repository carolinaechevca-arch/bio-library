package com.bio_library.user.application.ports.in;

import com.bio_library.user.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IStudentServicePort {
    Student createStudent(Student student);
    Page<Student> getStudents(String adminEmail, Pageable pageable);
    Student getStudent(Long userId);
    Student updateSanction(Long userId, Boolean active, LocalDate sanctionEndDate);
}
