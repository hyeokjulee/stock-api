package com.stock.api.kis.service;

import com.stock.api.kis.client.KisApiClient;
import com.stock.api.kis.dto.KisStockDto;
import com.stock.api.kis.dto.KisStocksResponse;
import com.stock.api.stock.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KisService {

    private final KisApiClient kisApiClient;

    public List<StockDto> getMarketCapRanking(String excd) {

        KisStocksResponse response = kisApiClient.fetchMarketCapRanking(excd);

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
