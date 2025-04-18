package com.stock.api.stock.controller;

import com.stock.api.stock.dto.StocksResponse;
import com.stock.api.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping("/market-cap-ranking")
    public ResponseEntity<StocksResponse> getStocksRankedByMarketCap() {

        StocksResponse stocksResponse = stockService.getStocksRankedByMarketCap();

        return ResponseEntity.ok(stocksResponse);
    }
}
