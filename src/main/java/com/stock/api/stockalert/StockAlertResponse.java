package com.stock.api.stockalert;

import java.time.LocalDateTime;

public record StockAlertResponse(
        Long id,
        String tickerSymbol,
        ExchangeCode exchangeCode,
        double targetPrice,
        LocalDateTime alertSentAt,
        LocalDateTime createdAt,
        double currentPrice
) {
    public static StockAlertResponse from(StockAlert alert, double currentPrice) {
        return new StockAlertResponse(
                alert.getId(),
                alert.getTickerSymbol(),
                alert.getExchangeCode(),
                alert.getTargetPrice(),
                alert.getAlertSentAt(),
                alert.getCreatedAt(),
                currentPrice
        );
    }
}