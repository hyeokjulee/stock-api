package com.stock.api.kis.service;

import com.stock.api.kis.client.KisApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KisTokenManager {

    private final KisApiClient kisApiClient;
    private final KisTokenRedisService kisTokenRedisService;

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshAccessToken() {

        String accessToken = kisApiClient.fetchAccessToken().getAccessToken();

        kisTokenRedisService.saveAccessToken(accessToken);
    }

    public String getAccessToken() {

        return kisTokenRedisService.getAccessToken();
    }
}
