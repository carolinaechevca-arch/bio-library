package com.bio_library.user.application.usecase;

import com.bio_library.user.application.ports.in.IStudentServicePort;
import com.bio_library.user.application.ports.out.IPasswordEncoderPersistencePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.application.ports.out.IUniversityFeignClientPort;
import com.bio_library.user.domain.model.Student;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.domain.service.StudentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class StudentUseCase implements IStudentServicePort {

    private final IStudentPersistencePort studentPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPort;
    private final StudentDomainService studentDomainService;
    private final IUniversityFeignClientPort universityServicePort;

    @Override
    public Student createStudent(Student student) {
        log.info("[CREATE] Registering student: {}", student.getUser().getEmail());

        boolean emailExists = studentPersistencePort.existsByEmail(student.getUser().getEmail());
        boolean dniExists = studentPersistencePort.existsByDni(student.getUser().getDni());
        studentDomainService.validateUniqueness(student, emailExists, dniExists);
        studentDomainService.validateEmailMatchesUniversity(student);

        String universityName = student.getUser().getUniversity().name();
        UniversityStudentData uniData = universityServicePort.getStudentByCarnetAndUniversity(
                student.getCarnet(), universityName);

        studentDomainService.validateUniversityData(student, uniData);

        Student verified = studentDomainService.applyUniversityData(student, uniData);

        String encodedPassword = passwordEncoderPort.encodePassword(verified.getUser().getPassword());
        Student toSave = studentDomainService.prepareStudentForRegistration(verified, encodedPassword);

        Student saved = studentPersistencePort.saveStudent(toSave);
        log.info("[CREATE-SUCCESS] Student created with ID: {}", saved.getUser().getId());
        return saved;
    }
}
