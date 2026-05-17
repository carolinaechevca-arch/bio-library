package com.bio_library.loans.infrastructure.adapters.driven.rabbitmq;

import com.bio_library.loans.application.ports.out.INotificationPort;
import com.bio_library.loans.infrastructure.adapters.driven.rabbitmq.dto.LoanNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRabbitMqAdapter implements INotificationPort {

    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Override
    public void notifyBookBorrowed(Long studentId, String studentEmail, String studentPhone, String bookId) {
        publish(studentId, studentEmail, studentPhone, bookId, "BOOK_BORROWED");
    }

    @Override
    public void notifyBookReturned(Long studentId, String studentEmail, String studentPhone, String bookId) {
        publish(studentId, studentEmail, studentPhone, bookId, "BOOK_RETURNED");
    }

    @Override
    public void notifyLoanUsageWarning(Long studentId, String studentEmail, String studentPhone, String bookId) {
        publish(studentId, studentEmail, studentPhone, bookId, "LOAN_USAGE_WARNING");
    }

    @Override
    public void notifyLoanUsageRevoked(Long studentId, String studentEmail, String studentPhone, String bookId) {
        publish(studentId, studentEmail, studentPhone, bookId, "LOAN_USAGE_REVOKED");
    }

    @Override
    public void notifyLoanExpired(Long studentId, String studentEmail, String studentPhone, String bookId) {
        publish(studentId, studentEmail, studentPhone, bookId, "LOAN_EXPIRED");
    }

    private void publish(Long studentId, String studentEmail, String studentPhone, String bookId, String eventType) {
        try {
            LoanNotificationMessage message = new LoanNotificationMessage(studentId, studentEmail, studentPhone, bookId, eventType);
            amqpTemplate.convertAndSend(exchange, routingKey, message);
            log.info("[RABBITMQ] Published event={} studentId={} bookId={}", eventType, studentId, bookId);
        } catch (Exception e) {
            log.warn("[RABBITMQ] Could not publish event={}: {}", eventType, e.getMessage());
        }
    }
}
