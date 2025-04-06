package com.stock.api.exception;

public class RefreshTokenMismatchException extends RuntimeException {
    public RefreshTokenMismatchException() {
        super("리프레시 토큰 불일치 또는 만료");
    }
}