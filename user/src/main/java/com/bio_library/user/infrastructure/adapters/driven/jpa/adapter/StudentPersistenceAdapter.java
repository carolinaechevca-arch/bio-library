package com.bio_library.user.infrastructure.adapters.driven.jpa.adapter;

import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.UserEntity;
import com.bio_library.user.infrastructure.adapters.driven.jpa.mapper.IStudentEntityMapper;
import com.bio_library.user.infrastructure.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.bio_library.user.infrastructure.adapters.driven.jpa.repository.IStudentRepository;
import com.bio_library.user.infrastructure.adapters.driven.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.bio_library.user.infrastructure.adapters.driven.util.PersistenceConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentPersistenceAdapter implements IStudentPersistencePort {

    private final IStudentRepository studentRepository;
    private final IUserRepository userRepository;
    private final IStudentEntityMapper studentEntityMapper;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public Student saveStudent(Student student) {
        log.info(STUDENT_SAVE_START, student.getUser().getEmail());

        UserEntity savedUser = userRepository.save(userEntityMapper.toEntity(student.getUser()));

        StudentEntity entity = studentEntityMapper.toEntity(student);
        entity.setUser(savedUser);

        StudentEntity saved = studentRepository.save(entity);
        log.info(STUDENT_SAVE_SUCCESS, saved.getId());

        return studentEntityMapper.toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info(STUDENT_EMAIL_CHECK, email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        log.info(STUDENT_DNI_CHECK, dni);
        return userRepository.existsByDni(dni);
    }

    @Override
    public Double findGpaByUserId(Long userId) {
        log.info("[STUDENT-DB] Finding GPA for userId={}", userId);
        return studentRepository.findByUser_Id(userId)
                .map(StudentEntity::getGpa)
                .orElse(null);
    }
}
