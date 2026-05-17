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
public class TwilioSmsAdapter implements ISmsNotificationPort {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhone;

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isBlank()) {
            Twilio.init(accountSid, authToken);
            log.info("[TWILIO] Initialized with phone={}", fromPhone);
        } else {
            log.warn("[TWILIO] No credentials configured — SMS disabled");
        }
    }

    @Override
    public void sendSms(String toPhone, String message) {
        if (accountSid == null || accountSid.isBlank()) {
            log.warn("[TWILIO] SMS skipped (no credentials): to={}", toPhone);
            return;
        }
        try {
            Message.creator(new PhoneNumber(toPhone), new PhoneNumber(fromPhone), message).create();
            log.info("[TWILIO] SMS sent to={}", toPhone);
        } catch (Exception e) {
            log.error("[TWILIO] Failed to send SMS to={}: {}", toPhone, e.getMessage());
        }
    }
}
