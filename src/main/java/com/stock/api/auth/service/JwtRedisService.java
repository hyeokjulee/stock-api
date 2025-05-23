package com.stock.api.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtRedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final long refreshTokenExpiration;

    private static final String JWT_REFRESH_TOKEN_PREFIX = "jwt:refresh_token:";

    public JwtRedisService(StringRedisTemplate stringRedisTemplate,
                           @Value("${jwt.refresh-token.expiration}") long refreshTokenExpiration) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public void saveRefreshToken(String email, String refreshToken) {
        stringRedisTemplate
                .opsForValue()
                .set(JWT_REFRESH_TOKEN_PREFIX + email, refreshToken, Duration.ofSeconds(refreshTokenExpiration));
    }

    public String getRefreshToken(String email) {
        return stringRedisTemplate.opsForValue().get(JWT_REFRESH_TOKEN_PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        stringRedisTemplate.delete(JWT_REFRESH_TOKEN_PREFIX + email);
    }
}