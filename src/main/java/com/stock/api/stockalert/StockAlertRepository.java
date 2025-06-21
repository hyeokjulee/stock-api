package com.stock.api.stockalert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {

    @Query("SELECT a FROM StockAlert a JOIN FETCH a.user WHERE a.alertSentAt IS NULL")
    List<StockAlert> findUnsentAlertsWithUser();

    List<StockAlert> findByUserId(Long userId);
}
