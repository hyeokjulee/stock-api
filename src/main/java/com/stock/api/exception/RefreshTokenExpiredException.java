package com.stock.api.exception;

public class RefreshTokenExpiredException extends RefreshTokenException {
    public RefreshTokenExpiredException() {
        super("Refresh Token이 만료되었습니다.");
    }
}