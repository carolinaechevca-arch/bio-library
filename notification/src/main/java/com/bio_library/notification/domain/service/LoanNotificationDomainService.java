package com.bio_library.notification.domain.service;

import com.bio_library.notification.domain.constants.DomainConstants;
import com.bio_library.notification.domain.model.LoanNotification;

public class LoanNotificationDomainService {

    public String buildSubject(LoanNotification notification) {
        return switch (notification.getEventType()) {
            case BOOK_BORROWED -> DomainConstants.BORROWED_SUBJECT;
            case BOOK_RETURNED -> DomainConstants.RETURNED_SUBJECT;
            case LOAN_USAGE_WARNING -> DomainConstants.USAGE_WARNING_SUBJECT;
            case LOAN_USAGE_REVOKED -> DomainConstants.USAGE_REVOKED_SUBJECT;
            case LOAN_EXPIRED -> DomainConstants.LOAN_EXPIRED_SUBJECT;
        };
    }

    public String buildBody(LoanNotification notification) {
        String template = switch (notification.getEventType()) {
            case BOOK_BORROWED -> DomainConstants.BORROWED_BODY;
            case BOOK_RETURNED -> DomainConstants.RETURNED_BODY;
            case LOAN_USAGE_WARNING -> DomainConstants.USAGE_WARNING_BODY;
            case LOAN_USAGE_REVOKED -> DomainConstants.USAGE_REVOKED_BODY;
            case LOAN_EXPIRED -> DomainConstants.LOAN_EXPIRED_BODY;
        };
        return String.format(template, notification.getBookId(), notification.getOccurredAt());
    }
}
