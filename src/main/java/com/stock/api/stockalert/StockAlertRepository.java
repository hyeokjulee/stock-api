package com.stock.api.stockalert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
}
