package com.bio_library.loans.application.ports.out;

public interface IJwtPort {
    String getUsernameFromToken(String token);
    boolean isTokenValid(String token, String username);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);
    Double getGpaFromToken(String token);
}
