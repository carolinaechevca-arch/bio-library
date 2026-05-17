package com.bio_library.notification.application.ports.in;

import com.bio_library.notification.domain.model.LoanEvent;

public interface INotificationServicePort {
    void handleLoanEvent(LoanEvent event);
}
