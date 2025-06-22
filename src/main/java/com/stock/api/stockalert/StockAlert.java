package com.stock.api.stockalert;

import com.stock.api.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "stock_alerts")
public class StockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ticker_symbol", length = 6, nullable = false)
    private String tickerSymbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_code", nullable = false)
    private ExchangeCode exchangeCode;

    @Column(name = "target_price", nullable = false)
    private double targetPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_direction", nullable = false)
    private AlertDirection alertDirection;

    @Column(name = "alert_sent_at")
    private LocalDateTime alertSentAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public StockAlert(User user, String tickerSymbol, ExchangeCode exchangeCode, double targetPrice, double currentPrice) {
        this.user = user;
        this.tickerSymbol = tickerSymbol;
        this.exchangeCode = exchangeCode;
        this.targetPrice = targetPrice;
        this.alertDirection = targetPrice > currentPrice ? AlertDirection.UP : AlertDirection.DOWN;
    }

    public void markAlertSent() {
        this.alertSentAt = LocalDateTime.now();
    }
}