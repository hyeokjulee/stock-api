package com.stock.api.auth.controller;

import com.stock.api.auth.dto.JwtResponse;
import com.stock.api.security.jwt.dto.JwtDto;
import com.stock.api.auth.dto.LoginRequest;
import com.stock.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final long refreshTokenExpiration;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieSameSite;

    public AuthController(AuthService authService,
                          @Value("${jwt.refresh-token.expiration}") long refreshTokenExpiration,
                          @Value("${cookie.http-only}") boolean cookieHttpOnly,
                          @Value("${cookie.secure}") boolean cookieSecure,
                          @Value("${cookie.same-site}") String cookieSameSite) {
        this.authService = authService;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {

        String naverAccessToken = loginRequest.getAccessToken();

        JwtDto jwtDto = authService.login(naverAccessToken);

        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtDto.getRefreshToken())
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .maxAge(refreshTokenExpiration)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new JwtResponse(jwtDto.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                               HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) { // 쿠키에 토큰이 없으면
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 그냥 401 코드 반환
        }

        JwtDto newJwtDto = authService.refresh(refreshToken);

        // 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newJwtDto.getRefreshToken())
                .httpOnly(cookieHttpOnly)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .maxAge(refreshTokenExpiration)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new JwtResponse(newJwtDto.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                       HttpServletResponse response) {

        if (refreshToken != null && !refreshToken.isBlank()) { // 쿠키에 토큰이 있으면
            authService.logout(refreshToken);

            // 쿠키 삭제
            ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(cookieHttpOnly)
                    .secure(cookieSecure)
                    .sameSite(cookieSameSite)
                    .maxAge(0)
                    .build();
            response.setHeader("Set-Cookie", cookie.toString());
        }

        return ResponseEntity.ok().build();
    }
}
