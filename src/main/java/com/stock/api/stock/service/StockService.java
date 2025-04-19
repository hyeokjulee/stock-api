package com.stock.api.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.api.kis.service.KisService;
import com.stock.api.stock.dto.StockDto;
import com.stock.api.stock.dto.StocksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {

    private final KisService kisService;
    private final StockRedisService stockRedisService;
    private final ObjectMapper objectMapper;

    public StocksResponse getStocksRankedByMarketCap() throws JsonProcessingException {

        String cachedStocks = stockRedisService.getStocks();

        if (cachedStocks != null) {
            return objectMapper.readValue(cachedStocks, StocksResponse.class);
        }

        List<StockDto> nasStocks = kisService.getMarketCapRanking("NAS");
        List<StockDto> nysStocks = kisService.getMarketCapRanking("NYS");

        String stocksJson = objectMapper.writeValueAsString(new StocksResponse(nasStocks, nysStocks));
        stockRedisService.saveStocks(stocksJson);

        return new StocksResponse(nasStocks, nysStocks);
    }
}
