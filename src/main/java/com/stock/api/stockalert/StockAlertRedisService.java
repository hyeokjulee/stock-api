package com.stock.api.stockalert;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class StockAlertRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "stock:current_price:";

    public void saveCurrentPrice(String ticker, String exchangeCode, double price) {
        String key = buildKey(ticker, exchangeCode);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(price), 30, TimeUnit.SECONDS);
    }

    public Double getCurrentPrice(String ticker, String exchangeCode) {
        String key = buildKey(ticker, exchangeCode);
        String value = stringRedisTemplate.opsForValue().get(key);
        return value != null ? Double.parseDouble(value) : null;
    }

    private String buildKey(String ticker, String exchangeCode) {
        return PREFIX + exchangeCode + ":" + ticker;
    }
}