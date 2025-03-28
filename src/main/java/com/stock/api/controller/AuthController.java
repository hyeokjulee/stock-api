package com.stock.api.controller;

import com.stock.api.dto.NaverAccessTokenDto;
import com.stock.api.dto.NaverUserDto;
import com.stock.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/userinfo")
    public ResponseEntity<NaverUserDto> getUserInfo(@RequestBody NaverAccessTokenDto naverAccessToken) {
        String accessToken = naverAccessToken.getAccessToken();
        NaverUserDto naverUserDto = authService.fetchNaverUserInfo(accessToken);

        return ResponseEntity.ok(naverUserDto);
    }
}
