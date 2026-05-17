package com.bio_library.user.infrastructure.adapters.driven.jpa.adapter;

import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.enums.University;
import com.bio_library.user.domain.exceptions.StudentNotFoundException;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.StudentEntity;
import com.bio_library.user.infrastructure.adapters.driven.jpa.entity.UserEntity;
import com.bio_library.user.infrastructure.adapters.driven.jpa.mapper.IStudentEntityMapper;
import com.bio_library.user.infrastructure.adapters.driven.jpa.mapper.IUserEntityMapper;
import com.bio_library.user.infrastructure.adapters.driven.jpa.repository.IStudentRepository;
import com.bio_library.user.infrastructure.adapters.driven.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        StudentEntity saved = studentRepository.save(
                studentEntityMapper.toEntity(student).toBuilder()
                        .user(savedUser)
                        .build()
        );
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

    @Override
    public Optional<Student> findByUserId(Long userId) {
        log.info("[STUDENT-DB] Finding student for userId={}", userId);
        return studentRepository.findByUser_Id(userId)
                .map(studentEntityMapper::toDomain);
    }

    @Override
    public Page<Student> findAll(University university, Pageable pageable) {
        log.info(STUDENT_FIND_ALL, university);
        Page<StudentEntity> page = Optional.ofNullable(university)
                .map(u -> studentRepository.findByUser_University(u, pageable))
                .orElseGet(() -> studentRepository.findAll(pageable));
        return page.map(studentEntityMapper::toDomain);
    }

    @Override
    public Student updateStudent(Student student) {
        log.info(STUDENT_UPDATE_SANCTION, student.getUser().getId());
        StudentEntity existing = studentRepository.findByUser_Id(student.getUser().getId())
                .orElseThrow(() -> new StudentNotFoundException(
                        String.format(DomainConstants.STUDENT_NOT_FOUND, student.getUser().getId())));
        StudentEntity updated = existing.toBuilder()
                .hasSanction(student.getHasSanction())
                .sanctionEndDate(student.getSanctionEndDate())
                .build();
        return studentEntityMapper.toDomain(studentRepository.save(updated));
    }

    @Override
    public List<Student> findExpiredSanctions() {
        log.info("[STUDENT-DB] Finding students with expired sanctions");
        return studentRepository.findByHasSanctionTrueAndSanctionEndDateBefore(LocalDate.now())
                .stream().map(studentEntityMapper::toDomain).toList();
    }
}
