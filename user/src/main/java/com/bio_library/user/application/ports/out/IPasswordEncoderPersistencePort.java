package com.bio_library.user.application.ports.out;

public interface IPasswordEncoderPersistencePort {
    String encodePassword(String password);
    Boolean matches(String rawPassword, String encodedPassword);
}
