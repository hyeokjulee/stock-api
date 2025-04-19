package com.stock.api.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class StockRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String STOCKS_MARKET_CAP_RANKING = "stocks:market_cap_ranking";

    public void saveStocks(String stocks) {
        stringRedisTemplate.opsForValue().set(STOCKS_MARKET_CAP_RANKING, stocks, 10, TimeUnit.MINUTES);
    }

    public String getStocks() {
        return stringRedisTemplate.opsForValue().get(STOCKS_MARKET_CAP_RANKING);
    }
}