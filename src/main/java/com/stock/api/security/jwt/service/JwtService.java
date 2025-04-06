package com.stock.api.security.jwt.service;

import com.stock.api.exception.RefreshTokenMismatchException;
import com.stock.api.security.jwt.dto.JwtDto;
import com.stock.api.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;

    public Authentication createAuthenticationFromToken(String accessToken) {

        String email = jwtUtil.extractEmailFromToken(accessToken);

        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    }

    public JwtDto createJwt(String email, String name) {

        String accessToken = jwtUtil.generateAccessToken(email, name);
        String refreshToken = jwtUtil.generateRefreshToken(email, name);

        jwtRedisService.saveRefreshToken(email, refreshToken);

        return new JwtDto(accessToken, refreshToken);
    }

    public JwtDto refreshJwtFromRefreshToken(String refreshToken) {

        String email = jwtUtil.extractEmailFromToken(refreshToken);
        String name = jwtUtil.extractNameFromToken(refreshToken);

        String storedRefreshToken = jwtRedisService.getRefreshToken(email);
        if (storedRefreshToken == null || !refreshToken.equals(storedRefreshToken)) {
            throw new RefreshTokenMismatchException();
        }

        JwtDto newJwtDto = createJwt(email, name);

        return newJwtDto;
    }
}
