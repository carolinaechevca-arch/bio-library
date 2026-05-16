package com.bio_library.notification.domain.model;

import com.bio_library.notification.domain.enums.LoanEventType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class LoanNotification {
    Long studentId;
    String studentEmail;
    String studentPhone;
    String bookId;
    LoanEventType eventType;
    LocalDateTime occurredAt;
}
