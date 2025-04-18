package com.stock.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockDto {

    private String exchange; // 거래소코드

    private String symbol; // 종목코드

    private String name; // 종목명

    private String lastPrice; // 현재가

    private String marketCap; // 시가총액

    private String rank; // 순위
}
