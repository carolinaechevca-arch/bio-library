package com.bio_library.notification.application.usecase;

import com.bio_library.notification.application.ports.in.INotificationServicePort;
import com.bio_library.notification.application.ports.out.ISmsNotificationPort;
import com.bio_library.notification.domain.constants.DomainConstants;
import com.bio_library.notification.domain.model.LoanEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NotificationUseCase implements INotificationServicePort {

    private final ISmsNotificationPort smsNotificationPort;

    @Override
    public void handleLoanEvent(LoanEvent event) {
        log.info("[NOTIFICATION] event={} studentId={} bookId={}", event.eventType(), event.studentId(), event.bookId());

        if (event.studentPhone() != null && !event.studentPhone().isBlank()) {
            String message = DomainConstants.buildSmsMessage(event.eventType(), event.bookId());
            smsNotificationPort.sendSms(event.studentPhone(), message);
        } else {
            log.warn("[NOTIFICATION] studentPhone is null for studentId={}, skipping SMS", event.studentId());
        }
    }
}
