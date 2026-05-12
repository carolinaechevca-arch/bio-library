package com.bio_library.user.application.usecase;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.application.ports.out.IPasswordEncoderPersistencePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.application.ports.out.IUniversityFeignClientPort;
import com.bio_library.user.domain.enums.University;
import com.bio_library.user.domain.factory.StudentFactory;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.domain.service.StudentDomainService;
import com.bio_library.user.domain.validation.StudentValidationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StudentUseCase implements IStudentServicePort {

    private final IStudentPersistencePort studentPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPort;
    private final StudentDomainService studentDomainService;
    private final IUniversityFeignClientPort universityServicePort;
    private final List<StudentValidationStrategy> strategies;

    @Override
    public Student createStudent(Student student) {
        log.info("[CREATE] Registering student: {}", student.getUser().getEmail());

        studentDomainService.validateUniqueness(student,
                studentPersistencePort.existsByEmail(student.getUser().getEmail()),
                studentPersistencePort.existsByDni(student.getUser().getDni()));

        UniversityStudentData uniData = universityServicePort.getStudentByCarnetAndUniversity(
                student.getCarnet(), student.getUser().getUniversity().name());

        strategies.forEach(s -> s.validate(student, uniData));

        Student saved = studentPersistencePort.saveStudent(
                StudentFactory.create(student, uniData,
                        passwordEncoderPort.encodePassword(student.getUser().getPassword())));

        log.info("[CREATE-SUCCESS] Student created with ID: {}", saved.getUser().getId());
        return saved;
    }

    @Override
    public Page<Student> getStudents(University university, Pageable pageable) {
        log.info("[LIST] Fetching students university={} page={}", university, pageable.getPageNumber());
        return studentPersistencePort.findAll(university, pageable);
    }

    @Override
    public Student getStudent(Long userId) {
        log.info("[GET] Fetching student userId={}", userId);
        return studentDomainService.validateStudentExists(
                studentPersistencePort.findByUserId(userId).orElse(null), userId);
    }

    @Override
    public Student updateSanction(Long userId, Boolean active, LocalDateTime sanctionEndDate) {
        log.info("[SANCTION] Updating sanction userId={} active={}", userId, active);
        Student student = studentDomainService.validateStudentExists(
                studentPersistencePort.findByUserId(userId).orElse(null), userId);
        return studentPersistencePort.updateStudent(student.withSanction(active, sanctionEndDate));
    }
}
