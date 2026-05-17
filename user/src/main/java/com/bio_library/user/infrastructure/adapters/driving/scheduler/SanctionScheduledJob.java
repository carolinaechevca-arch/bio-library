package com.bio_library.user.infrastructure.adapters.driving.scheduler;

import com.bio_library.user.application.ports.in.ISanctionSchedulerServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SanctionScheduledJob {

    private final ISanctionSchedulerServicePort sanctionSchedulerServicePort;

    @Scheduled(fixedRate = 300000)
    public void liftExpiredSanctionsJob() {
        log.info("[JOB] liftExpiredSanctionsJob started");
        sanctionSchedulerServicePort.liftExpiredSanctions();
        log.info("[JOB] liftExpiredSanctionsJob finished");
    }
}
