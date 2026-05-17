package com.bio_library.notification.infrastructure.adapters.driving.rabbitmq;

import com.bio_library.notification.application.ports.in.INotificationServicePort;
import com.bio_library.notification.domain.model.LoanEvent;
import com.bio_library.notification.infrastructure.adapters.driving.rabbitmq.dto.LoanNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanEventListener {

    private final INotificationServicePort notificationServicePort;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void onLoanEvent(LoanNotificationMessage message) {
        log.info("[RABBITMQ] Received event={} studentId={}", message.eventType(), message.studentId());
        LoanEvent event = new LoanEvent(
                message.studentId(),
                message.studentEmail(),
                message.studentPhone(),
                message.bookId(),
                message.eventType()
        );
        notificationServicePort.handleLoanEvent(event);
    }
}
