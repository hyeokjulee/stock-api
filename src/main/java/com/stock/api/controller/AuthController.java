package com.stock.api.controller;

import com.stock.api.security.dto.JwtDto;
import com.stock.api.dto.NaverAccessTokenDto;
import com.stock.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody NaverAccessTokenDto naverAccessToken) {
        String accessToken = naverAccessToken.getAccessToken();

        JwtDto jwtDto = authService.login(accessToken);

        return ResponseEntity.ok(jwtDto);
    }
}
