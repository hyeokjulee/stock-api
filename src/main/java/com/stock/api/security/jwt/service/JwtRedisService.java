package com.stock.api.security.jwt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtRedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final long refreshTokenExpirationTime;

    private static final String JWT_REFRESH_PREFIX = "jwt:refresh:";

    public JwtRedisService(StringRedisTemplate stringRedisTemplate,
                           @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpirationTime) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public void saveRefreshToken(String email, String refreshToken) {
        stringRedisTemplate.opsForValue().set(JWT_REFRESH_PREFIX + email, refreshToken, Duration.ofSeconds(refreshTokenExpirationTime));
    }

    public String getRefreshToken(String email) {
        return stringRedisTemplate.opsForValue().get(JWT_REFRESH_PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        stringRedisTemplate.delete(JWT_REFRESH_PREFIX + email);
    }
}