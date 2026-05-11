package com.bio_library.user.application.ports.in;

import com.bio_library.user.domain.model.Student;

public interface IStudentServicePort {
    Student createStudent(Student student);

}