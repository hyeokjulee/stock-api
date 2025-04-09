package com.stock.api.auth.controller;

import com.stock.api.auth.dto.JwtResponse;
import com.stock.api.auth.dto.JwtDto;
import com.stock.api.auth.dto.LoginRequest;
import com.stock.api.auth.service.AuthService;
import com.stock.api.auth.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {

        String naverAccessToken = loginRequest.getAccessToken();

        JwtDto jwtDto = authService.login(naverAccessToken);

        cookieUtil.setRefreshToken(response, jwtDto.getRefreshToken());

        return ResponseEntity.ok(new JwtResponse(jwtDto.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                               HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) { // 쿠키에 토큰이 없으면
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 그냥 401 코드 반환
        }

        JwtDto jwtDto = authService.refresh(refreshToken);

        cookieUtil.setRefreshToken(response, jwtDto.getRefreshToken());

        return ResponseEntity.ok(new JwtResponse(jwtDto.getAccessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                       HttpServletResponse response) {

        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);

            cookieUtil.clearRefreshToken(response);
        }

        return ResponseEntity.ok().build();
    }
}
