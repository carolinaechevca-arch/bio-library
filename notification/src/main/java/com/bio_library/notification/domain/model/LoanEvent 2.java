package com.bio_library.notification.domain.model;

public record LoanEvent(
        Long studentId,
        String studentEmail,
        String studentPhone,
        String bookId,
        String eventType
) {}
