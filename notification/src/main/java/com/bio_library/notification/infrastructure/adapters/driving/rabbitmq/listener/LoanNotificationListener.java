package com.bio_library.notification.infrastructure.adapters.driving.rabbitmq.listener;

import com.bio_library.notification.application.ports.in.ILoanNotificationServicePort;
import com.bio_library.notification.domain.enums.LoanEventType;
import com.bio_library.notification.domain.model.LoanNotification;
import com.bio_library.notification.infrastructure.adapters.driving.rabbitmq.dto.LoanNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanNotificationListener {

    private final ILoanNotificationServicePort notificationServicePort;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void handleLoanEvent(LoanNotificationMessage message) {
        log.info("[RABBITMQ] Received event={} studentId={} bookId={}",
                message.eventType(), message.studentId(), message.bookId());
        LoanNotification notification = LoanNotification.builder()
                .studentId(message.studentId())
                .studentEmail(message.studentEmail())
                .studentPhone(message.studentPhone())
                .bookId(message.bookId())
                .eventType(LoanEventType.valueOf(message.eventType()))
                .occurredAt(LocalDateTime.now())
                .build();
        notificationServicePort.notify(notification);
    }
}
