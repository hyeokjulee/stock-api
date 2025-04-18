package com.stock.api.stock.service;

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

    public StocksResponse getStocksRankedByMarketCap() {

        List<StockDto> nasStocks = kisService.getMarketCapRanking("NAS");
        List<StockDto> nysStocks = kisService.getMarketCapRanking("NYS");

        return new StocksResponse(nasStocks, nysStocks);
    }
}
