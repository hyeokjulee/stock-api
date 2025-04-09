package com.stock.api.exception;

public class RefreshTokenMismatchException extends RefreshTokenException {
    public RefreshTokenMismatchException() {
        super("Refresh Token이 일치하지 않습니다.");
    }
}