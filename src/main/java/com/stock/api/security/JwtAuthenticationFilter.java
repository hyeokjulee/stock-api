package com.stock.api.security;

import com.stock.api.auth.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/auth/login") || requestURI.equals("/auth/refresh") || requestURI.equals("/auth/logout")) {
            filterChain.doFilter(request, response);

            return;
        }

        String accessToken = getTokenFromRequest(request);
        try {
            Authentication authentication = jwtService.createAuthenticationFromAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) { // 만료된 액세스 토큰이면
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
