package com.stock.api.auth.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private final long refreshTokenExpiration;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieSameSite;

    public CookieUtil(@Value("${jwt.refresh-token.expiration}") long refreshTokenExpiration,
                      @Value("${cookie.http-only}") boolean cookieHttpOnly,
                      @Value("${cookie.secure}") boolean cookieSecure,
                      @Value("${cookie.same-site}") String cookieSameSite) {
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    public void clearRefreshToken(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .maxAge(0)
                .path("/")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }
}
