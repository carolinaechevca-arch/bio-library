package com.bio_library.user.domain.model;

public record AuthenticationModel(
        String email,
        String password
) {
}