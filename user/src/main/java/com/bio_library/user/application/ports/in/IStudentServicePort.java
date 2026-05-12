package com.bio_library.user.application.ports.in;

import com.bio_library.user.domain.enums.University;
import com.bio_library.user.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IStudentServicePort {
    Student createStudent(Student student);
    Page<Student> getStudents(University university, Pageable pageable);
    Student getStudent(Long userId);
    Student updateSanction(Long userId, Boolean active, LocalDateTime sanctionEndDate);
}
