package com.stock.api.stockalert;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockAlertRequest {

    private String tickerSymbol;

    private ExchangeCode exchangeCode;

    private double targetPrice;
}
