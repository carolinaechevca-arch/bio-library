package com.bio_library.user.application.usecase;

import com.bio_library.user.application.ports.in.ISanctionSchedulerServicePort;
import com.bio_library.user.application.ports.out.IStudentPersistencePort;
import com.bio_library.user.domain.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SanctionSchedulerUseCase implements ISanctionSchedulerServicePort {

    private final IStudentPersistencePort studentPersistencePort;

    @Override
    public void liftExpiredSanctions() {
        List<Student> expired = studentPersistencePort.findExpiredSanctions();
        log.info("[SCHEDULER] Lifting {} expired sanctions", expired.size());
        expired.forEach(student -> {
            studentPersistencePort.updateStudent(student.withSanction(false, null));
            log.info("[SCHEDULER] Sanction lifted for studentId={}", student.getUser().getId());
        });
    }
}
