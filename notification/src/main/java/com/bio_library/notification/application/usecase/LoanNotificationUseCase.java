package com.bio_library.notification.application.usecase;

import com.bio_library.notification.application.ports.in.ILoanNotificationServicePort;
import com.bio_library.notification.application.ports.out.ISmsNotificationPort;
import com.bio_library.notification.domain.model.LoanNotification;
import com.bio_library.notification.domain.service.LoanNotificationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoanNotificationUseCase implements ILoanNotificationServicePort {

    private final ISmsNotificationPort smsNotificationPort;
    private final LoanNotificationDomainService domainService;

    @Override
    public void notify(LoanNotification notification) {
        log.info("[NOTIFICATION] Processing event={} studentId={} bookId={}",
                notification.getEventType(), notification.getStudentId(), notification.getBookId());
        String subject = domainService.buildSubject(notification);
        String body = domainService.buildBody(notification);
        String message = subject + "\n" + body;
        smsNotificationPort.send(notification.getStudentPhone(), message);
    }
}
