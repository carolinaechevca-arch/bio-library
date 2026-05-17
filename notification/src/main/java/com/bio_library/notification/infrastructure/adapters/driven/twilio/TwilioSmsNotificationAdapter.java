package com.bio_library.notification.infrastructure.adapters.driven.twilio;

import com.bio_library.notification.application.ports.out.ISmsNotificationPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioSmsNotificationAdapter implements ISmsNotificationPort {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isBlank()) {
            Twilio.init(accountSid, authToken);
            log.info("[TWILIO] Initialized with number={}", fromPhoneNumber);
        } else {
            log.warn("[TWILIO] No credentials configured — SMS disabled");
        }
    }

    @Override
    public void send(String toPhoneNumber, String message) {
        if (accountSid == null || accountSid.isBlank()) {
            log.warn("[SMS] Twilio not configured, skipping notification to={}", toPhoneNumber);
            return;
        }
        if (toPhoneNumber == null || toPhoneNumber.isBlank()) {
            log.warn("[SMS] No phone number available, skipping notification");
            return;
        }
        try {
            log.info("[SMS] Sending to={}", toPhoneNumber);
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();
            log.info("[SMS] Sent successfully to={}", toPhoneNumber);
        } catch (Exception e) {
            log.error("[SMS] Failed to send to={}: {}", toPhoneNumber, e.getMessage());
        }
    }
}
