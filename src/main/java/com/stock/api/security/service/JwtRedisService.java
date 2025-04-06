package com.stock.api.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class JwtRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String JWT_REFRESH_PREFIX = "jwt:refresh:";

    public void saveRefreshToken(String email, String refreshToken, Duration ttl) {
        stringRedisTemplate.opsForValue().set(JWT_REFRESH_PREFIX + email, refreshToken, ttl);
    }

    public String getRefreshToken(String email) {
        return stringRedisTemplate.opsForValue().get(JWT_REFRESH_PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        stringRedisTemplate.delete(JWT_REFRESH_PREFIX + email);
    }
}