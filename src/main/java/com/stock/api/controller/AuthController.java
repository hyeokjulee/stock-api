package com.stock.api.controller;

import com.stock.api.dto.NaverResponseDto;
import com.stock.api.dto.NaverUserDto;
import com.stock.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/userinfo")
    public ResponseEntity<NaverUserDto> getUserInfo(@RequestBody String naverAccessToken) {


        NaverUserDto naverUserDto = authService.fetchNaverUserInfo(naverAccessToken);

        return ResponseEntity.ok(naverUserDto);
    }
}
