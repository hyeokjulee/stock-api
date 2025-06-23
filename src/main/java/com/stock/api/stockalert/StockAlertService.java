package com.stock.api.stockalert;

import com.stock.api.email.EmailService;
import com.stock.api.emaillog.EmailLog;
import com.stock.api.emaillog.EmailLogService;
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
    private final EmailService emailService;
    private final EmailLogService emailLogService;

    public void safelyDeleteStockAlert(Long id, Long userId) {
        stockAlertRepository.findById(id).ifPresent(stockAlert -> { // 동시성 문제 방지
            if (stockAlert.getUser().getId().equals(userId)) { // 악의적 접근 방어
                stockAlertRepository.delete(stockAlert);
            }
        });
    }

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

    public double getCachedOrFetchCurrentPrice(String ticker, String exchangeCode) {
        Double cached = stockAlertRedisService.getCurrentPrice(ticker, exchangeCode);
        if (cached != null) return cached;

        double price = kisService.getCurrentPrice(ticker, exchangeCode);
        stockAlertRedisService.saveCurrentPrice(ticker, exchangeCode, price);
        return price;
    }

    @Transactional
    public void sendAndMarkAlert(StockAlert alert, double currentPrice) {
        String email = alert.getUser().getEmail();
        String subject = String.format("[주가 알림] %s (%s) 목표 도달!", alert.getTickerSymbol(), alert.getExchangeCode().name());
        String content = String.format("현재 주가: %.2f\n목표 주가: %.2f", currentPrice, alert.getTargetPrice());

        boolean success = false;
        String errorMessage = null;

        try {
            emailService.send(email, subject, content);
            success = true;
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        EmailLog log = new EmailLog(
                alert.getId(),
                alert.getUser().getId(),
                subject,
                content,
                success,
                errorMessage
        );
        emailLogService.saveEmailLog(log);

        if (success) {
            alert.markAlertSent();
            stockAlertRepository.save(alert);
        }
    }

    public List<StockAlert> getUnsentAlertsWithUser() {
        return stockAlertRepository.findUnsentAlertsWithUser();
    }
}