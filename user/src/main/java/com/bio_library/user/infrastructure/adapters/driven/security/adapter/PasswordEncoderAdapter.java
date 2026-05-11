package com.bio_library.user.infrastructure.adapters.driven.security.adapter;

import com.bio_library.user.application.ports.out.IPasswordEncoderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoderPersistencePort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
