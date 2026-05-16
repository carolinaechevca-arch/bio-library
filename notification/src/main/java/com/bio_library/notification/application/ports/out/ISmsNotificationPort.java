package com.bio_library.notification.application.ports.out;

public interface ISmsNotificationPort {
    void send(String toPhoneNumber, String message);
}
