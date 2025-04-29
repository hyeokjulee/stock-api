package com.stock.api.stockalert;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockAlertRequest {

    @NotBlank
    @Size(max = 6)
    private String tickerSymbol;

    @NotNull
    private ExchangeCode exchangeCode;

    @Positive
    @Max(999999)
    private double targetPrice;
}
