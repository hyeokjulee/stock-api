package com.stock.api.security.jwt.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final int accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtUtil(@Value("${jwt.secret-key}") String secret,
                   @Value("${jwt.access-token-expiration-time}") int accessTokenExpirationTime,
                   @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String extractEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractNameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }

    public String generateAccessToken(String email, String name) {
        return Jwts.builder()
                .subject(email)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime * 1000))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String email, String name) {
        return Jwts.builder()
                .subject(email)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime * 1000))
                .signWith(secretKey)
                .compact();
    }
}
