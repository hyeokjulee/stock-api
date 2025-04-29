package com.stock.api.stockalert;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock-alerts")
public class StockAlertController {

    private final StockAlertService stockAlertService;

    @PostMapping("/create")
    public ResponseEntity<Void> createStockAlert(@Valid @RequestBody StockAlertRequest stockAlertRequest,
                                                   @AuthenticationPrincipal Long userId) {

        stockAlertService.createStockAlert(stockAlertRequest, userId);

        return ResponseEntity.ok().build();
    }
}
