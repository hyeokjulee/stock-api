package com.stock.api.kis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KisStocksResponse {

    private List<KisStockDto> output2;
}
