package com.stock.api.auth.service;

import com.stock.api.exception.RefreshTokenExpiredException;
import com.stock.api.exception.RefreshTokenMismatchException;
import com.stock.api.auth.dto.JwtDto;
import com.stock.api.auth.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtUtil jwtUtil;
    private final JwtRedisService jwtRedisService;

    public Authentication createAuthenticationFromAccessToken(String accessToken) {

        Long id = jwtUtil.extractIdFromAccessToken(accessToken);

        return new UsernamePasswordAuthenticationToken(id, null, Collections.emptyList());
    }

    public JwtDto createJwt(Long id, String email, String name) {

        String accessToken = jwtUtil.generateAccessToken(id, email, name);
        String refreshToken = jwtUtil.generateRefreshToken(id, email, name);

        jwtRedisService.saveRefreshToken(email, refreshToken);

        return new JwtDto(accessToken, refreshToken);
    }

    public JwtDto refreshJwtFromRefreshToken(String refreshToken) {

        Long id;
        String email;
        String name;

        try {
            id = jwtUtil.extractIdFromRefreshToken(refreshToken);
            email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
            name = jwtUtil.extractNameFromRefreshToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }

        String storedRefreshToken = jwtRedisService.getRefreshToken(email);
        if (storedRefreshToken == null || !refreshToken.equals(storedRefreshToken)) {
            throw new RefreshTokenMismatchException();
        }

        return createJwt(id, email, name);
    }

    public void logoutFromRefreshToken(String refreshToken) {

        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);

        jwtRedisService.deleteRefreshToken(email);
    }
}
