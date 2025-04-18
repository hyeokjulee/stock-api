package com.stock.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StocksResponse {

    private List<StockDto> nasStocks;

    private List<StockDto> nysStocks;
}
