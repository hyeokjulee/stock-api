package com.stock.api.auth.controller;

import com.stock.api.security.dto.JwtAccessTokenDto;
import com.stock.api.security.dto.JwtDto;
import com.stock.api.auth.dto.NaverAccessTokenDto;
import com.stock.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtAccessTokenDto> login(@RequestBody NaverAccessTokenDto naverAccessToken,
                                                   HttpServletResponse response) {

        String accessToken = naverAccessToken.getAccessToken();

        JwtDto jwtDto = authService.login(accessToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtDto.getRefreshToken())
                .httpOnly(false)
                .secure(false)
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new JwtAccessTokenDto(jwtDto.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAccessTokenDto> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                     HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) { // 쿠키에 토큰이 없으면
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 그냥 401 코드 반환
        }

        JwtDto jwtDto = authService.refresh(refreshToken);

        // 쿠키

        return ResponseEntity.ok(new JwtAccessTokenDto(jwtDto.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        // 쿠키 삭제

        return ResponseEntity.ok().build();
    }
}
