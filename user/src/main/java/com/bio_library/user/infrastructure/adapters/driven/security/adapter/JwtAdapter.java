package com.bio_library.user.infrastructure.adapters.driven.security.adapter;

import com.bio_library.user.application.ports.out.IJwtPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtAdapter implements IJwtPersistencePort {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    public String generateAccessToken(String email, Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    @Override
    public Long getAccessExpirationTime() {
        return expiration;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}