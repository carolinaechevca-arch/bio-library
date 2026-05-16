package com.bio_library.notification.infrastructure.adapters.driving.rabbitmq.dto;

public record LoanNotificationMessage(
        Long studentId,
        String studentEmail,
        String studentPhone,
        String bookId,
        String eventType
) {}
