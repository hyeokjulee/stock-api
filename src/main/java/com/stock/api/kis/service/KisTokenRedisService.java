package com.stock.api.kis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KisTokenRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String KIS_ACCESS_TOKEN = "kis:access_token";

    public void saveAccessToken(String accessToken) {
        stringRedisTemplate.opsForValue().set(KIS_ACCESS_TOKEN, accessToken);
    }

    public String getAccessToken() {
        return stringRedisTemplate.opsForValue().get(KIS_ACCESS_TOKEN);
    }
}