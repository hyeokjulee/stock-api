package com.stock.api.stockalert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {

    List<StockAlert> findByUserId(Long userId);
}
