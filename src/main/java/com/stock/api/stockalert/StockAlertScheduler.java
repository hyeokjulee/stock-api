package com.stock.api.stockalert;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StockAlertScheduler {

    private final StockAlertService stockAlertService;

    @Scheduled(cron = "0 */10 * * * *") // 매 10분마다 실행
    public void checkStockAlerts() {
        List<StockAlert> stockAlerts = stockAlertService.getUnsentAlertsWithUser();

        for (StockAlert alert : stockAlerts) {
            double currentPrice =
                    stockAlertService.getCachedOrFetchCurrentPrice(alert.getTickerSymbol(), alert.getExchangeCode().name());

            if (shouldAlert(alert, currentPrice)) {
                stockAlertService.sendAndMarkAlert(alert, currentPrice);
            }
        }
    }

    private boolean shouldAlert(StockAlert alert, double currentPrice) {
        return (alert.getAlertDirection() == AlertDirection.UP && currentPrice >= alert.getTargetPrice())
                || (alert.getAlertDirection() == AlertDirection.DOWN && currentPrice <= alert.getTargetPrice());
    }
}