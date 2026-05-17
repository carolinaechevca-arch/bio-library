package com.bio_library.loans.infrastructure.adapters.driven.rabbitmq.dto;

public record LoanNotificationMessage(
        Long studentId,
        String studentEmail,
        String studentPhone,
        String bookId,
        String eventType
) {}
