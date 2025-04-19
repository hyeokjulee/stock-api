package com.stock.api.kis.service;

import com.stock.api.kis.client.KisApiClient;
import com.stock.api.kis.dto.KisStockDto;
import com.stock.api.kis.dto.KisStocksResponse;
import com.stock.api.stock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KisService {

    private final KisApiClient kisApiClient;
    private final KisTokenManager kisTokenManager;

    public List<StockDto> getMarketCapRanking(String exchangeCode) {

        String accessToken = kisTokenManager.getAccessToken();

        KisStocksResponse response = kisApiClient.fetchMarketCapRanking(exchangeCode, accessToken);

        return response
                .getOutput2()
                .stream()
                .map(this::convertToStockDto)
                .collect(Collectors.toList());
    }

    private StockDto convertToStockDto(KisStockDto kisStockDto) {
        return new StockDto(
                kisStockDto.getExcd(),
                kisStockDto.getSymb(),
                kisStockDto.getName(),
                kisStockDto.getLast(),
                kisStockDto.getTomv(),
                kisStockDto.getRank()
        );
    }
}
