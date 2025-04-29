package com.stock.api.stockalert;

import com.stock.api.kis.service.KisService;
import com.stock.api.user.User;
import com.stock.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockAlertService {

    private final StockAlertRepository stockAlertRepository;
    private final UserRepository userRepository;
    private final KisService kisService;

    public void createStockAlert(StockAlertRequest stockAlertRequest, Long userId) {

        String tickerSymbol = stockAlertRequest.getTickerSymbol();
        ExchangeCode exchangeCode = stockAlertRequest.getExchangeCode();
        double targetPrice = stockAlertRequest.getTargetPrice();

        double currentPrice = kisService.getCurrentPrice(tickerSymbol, exchangeCode.name());
        AlertDirection alertDirection = targetPrice > currentPrice ? AlertDirection.UP : AlertDirection.DOWN;

        User user = userRepository.findById(userId).get();

        StockAlert stockAlert = new StockAlert(user, tickerSymbol, exchangeCode, targetPrice, alertDirection);
        stockAlertRepository.save(stockAlert);
    }
}