package com.bio_library.university_mock.application.usecase;

import com.bio_library.university_mock.domain.enums.University;
import com.bio_library.university_mock.domain.model.UniversityStudent;
import com.bio_library.university_mock.application.ports.in.IUniversityStudentServicePort;
import com.bio_library.university_mock.application.ports.out.IUniversityStudentPersistencePort;
import com.bio_library.university_mock.domain.service.UniversityStudentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UniversityStudentUseCase implements IUniversityStudentServicePort {

    private final IUniversityStudentPersistencePort persistencePort;
    private final UniversityStudentDomainService domainService;

    @Override
    public UniversityStudent getStudentByCarnetAndUniversity(String carnet, University university) {
        log.info("[USE-CASE] Querying student by carnet: {} university: {}", carnet, university);
        UniversityStudent student = persistencePort.findByCarnetAndUniversity(carnet, university);
        return domainService.validateStudentExists(student, carnet);
    }

    @Override
    public List<UniversityStudent> getAllStudentsByUniversity(University university) {
        log.info("[USE-CASE] Querying all students for university: {}", university);
        List<UniversityStudent> students = persistencePort.findAllByUniversity(university);
        return students;
    }
}
