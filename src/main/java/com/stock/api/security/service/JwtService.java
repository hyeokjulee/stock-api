package com.stock.api.security.service;

import com.stock.api.security.dto.JwtDto;
import com.stock.api.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtUtil jwtUtil;

    public Authentication createAuthentication(String accessToken) {

        String email = jwtUtil.extractEmail(accessToken);

        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    }

    public JwtDto createJwtDto(String email, String name) {

        String accessToken = jwtUtil.generateAccessToken(email, name);
        String refreshToken = jwtUtil.generateRefreshToken(email, name);

        return new JwtDto(accessToken, refreshToken);
    }
}
