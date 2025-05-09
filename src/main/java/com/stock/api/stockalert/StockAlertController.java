package com.stock.api.stockalert;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock-alerts")
public class StockAlertController {

    private final StockAlertService stockAlertService;

    @PostMapping
    public ResponseEntity<Void> createStockAlert(@Valid @RequestBody StockAlertRequest stockAlertRequest,
                                                   @AuthenticationPrincipal Long userId) {

        stockAlertService.createStockAlert(stockAlertRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<StockAlertResponse>> getStockAlerts(@AuthenticationPrincipal Long userId) {

        List<StockAlertResponse> alerts = stockAlertService.getStockAlerts(userId);

        return ResponseEntity.ok(alerts);
    }
}