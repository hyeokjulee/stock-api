package com.stock.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String allowedOrigin;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(@Value("${cors.allowed-origin}") String allowedOrigin,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.allowedOrigin = allowedOrigin;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 활성화
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));

        // csrf 비활성화
        http.csrf(auth -> auth.disable());

        // form 로그인 방식 비활성화
        http.formLogin(auth -> auth.disable());

        // http basic 인증 방식 비활성화
        http.httpBasic(auth -> auth.disable());

        // 인증되지 않은 사용자의 AnonymousAuthenticationToken 비활성화
        http.anonymous(auth -> auth.disable());

        // 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/*", "/error", "/stocks/*").permitAll()
                .anyRequest().authenticated());
        
        // JWT 검증 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 세션(상태 유지) 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정

        return source;
    }
}
