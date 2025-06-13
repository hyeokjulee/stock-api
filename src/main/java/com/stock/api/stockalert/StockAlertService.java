package com.stock.api.stockalert;

import com.stock.api.exception.InvalidStockInputException;
import com.stock.api.kis.service.KisService;
import com.stock.api.user.User;
import com.stock.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockAlertService {

    private final StockAlertRepository stockAlertRepository;
    private final UserRepository userRepository;
    private final KisService kisService;
    private final StockAlertRedisService stockAlertRedisService;

    @Transactional
    public void createStockAlert(StockAlertRequest stockAlertRequest, Long userId) {

        String tickerSymbol = stockAlertRequest.getTickerSymbol();
        ExchangeCode exchangeCode = stockAlertRequest.getExchangeCode();
        double targetPrice = stockAlertRequest.getTargetPrice();
        double currentPrice = kisService.getCurrentPrice(tickerSymbol, exchangeCode.name());

        if (currentPrice == 0.0) {
            throw new InvalidStockInputException();
        }

        User user = userRepository.findById(userId).get();

        StockAlert stockAlert = new StockAlert(user, tickerSymbol, exchangeCode, targetPrice, currentPrice);
        stockAlertRepository.save(stockAlert);
    }

    @Transactional
    public List<StockAlertResponse> getStockAlerts(Long userId) {
        List<StockAlert> alerts = stockAlertRepository.findByUserId(userId);

        return alerts.stream()
                .map(alert -> StockAlertResponse.from(
                        alert, getCachedOrFetchCurrentPrice(alert.getTickerSymbol(), alert.getExchangeCode().name())
                ))
                .collect(Collectors.toList());
    }

    private double getCachedOrFetchCurrentPrice(String ticker, String exchangeCode) {
        Double cached = stockAlertRedisService.getCurrentPrice(ticker, exchangeCode);
        if (cached != null) return cached;

        double price = kisService.getCurrentPrice(ticker, exchangeCode);
        stockAlertRedisService.saveCurrentPrice(ticker, exchangeCode, price);
        return price;
    }
}