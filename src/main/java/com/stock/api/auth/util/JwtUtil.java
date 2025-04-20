package com.stock.api.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey accessTokenSecretKey;

    private final SecretKey refreshTokenSecretKey;
    private final int accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(@Value("${jwt.access-token.secret}") String accessTokenSecret,
                   @Value("${jwt.refresh-token.secret}") String refreshTokenSecret,
                   @Value("${jwt.access-token.expiration}") int accessTokenExpiration,
                   @Value("${jwt.refresh-token.expiration}") long refreshTokenExpiration) {
        this.accessTokenSecretKey = new SecretKeySpec(accessTokenSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.refreshTokenSecretKey = new SecretKeySpec(refreshTokenSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public Long extractIdFromAccessToken(String accessToken) {

        return Long.parseLong(parseClaims(accessTokenSecretKey, accessToken).getSubject());
    }

    public Long extractIdFromRefreshToken(String refreshToken) {

        return Long.parseLong(parseClaims(refreshTokenSecretKey, refreshToken).getSubject());
    }

    public String extractEmailFromRefreshToken(String refreshToken) {

        return parseClaims(refreshTokenSecretKey, refreshToken).get("email", String.class);
    }

    public String extractNameFromRefreshToken(String refreshToken) {

        return parseClaims(refreshTokenSecretKey, refreshToken).get("name", String.class);
    }

    public String generateAccessToken(Long id, String email, String name) {

        return generateToken(id, email, name, accessTokenExpiration, accessTokenSecretKey);
    }

    public String generateRefreshToken(Long id, String email, String name) {

        return generateToken(id, email, name, refreshTokenExpiration, refreshTokenSecretKey);
    }

    private Claims parseClaims(SecretKey secretKey, String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(Long id, String email, String name, long expiration, SecretKey secretKey) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("email", email)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey)
                .compact();
    }
}
