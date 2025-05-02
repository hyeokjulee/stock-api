package com.stock.api.exception;

public class InvalidStockInputException extends RuntimeException {
    public InvalidStockInputException() {
        super("입력한 정보에 해당하는 주식이 존재하지 않습니다.");
    }
}