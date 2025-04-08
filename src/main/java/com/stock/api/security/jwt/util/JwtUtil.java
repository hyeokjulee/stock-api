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

    public String extractEmailFromAccessToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(accessTokenSecretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject();
    }

    public String extractEmailFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith(refreshTokenSecretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .getSubject();
    }

    public String extractNameFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith(refreshTokenSecretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .get("name", String.class);
    }

    public String generateAccessToken(String email, String name) {
        return Jwts.builder()
                .subject(email)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String generateRefreshToken(String email, String name) {
        return Jwts.builder()
                .subject(email)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
                .signWith(refreshTokenSecretKey)
                .compact();
    }
}
