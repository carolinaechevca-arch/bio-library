package com.bio_library.notification.application.ports.in;

import com.bio_library.notification.domain.model.LoanNotification;

public interface ILoanNotificationServicePort {
    void notify(LoanNotification notification);
}
