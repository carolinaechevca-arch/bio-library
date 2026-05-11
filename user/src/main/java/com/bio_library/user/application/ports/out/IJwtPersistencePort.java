package com.bio_library.user.application.ports.out;

public interface IJwtPersistencePort {

    String generateAccessToken(String email, Long userId, String role);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, String username);

    Long getAccessExpirationTime();
}