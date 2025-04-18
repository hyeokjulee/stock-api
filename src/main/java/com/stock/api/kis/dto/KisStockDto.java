package com.stock.api.kis.dto;

import lombok.Getter;

@Getter
public class KisStockDto {

    private String excd; // 거래소코드

    private String symb; // 종목코드

    private String name; // 종목명

    private String last; // 현재가

    private String tomv; // 시가총액

    private String rank; // 순위
}
