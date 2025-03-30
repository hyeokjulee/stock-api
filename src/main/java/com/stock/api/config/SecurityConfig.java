package com.stock.api.config;

import com.stock.api.jwt.JwtAuthenticationFilter;
import com.stock.api.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf 비활성화
        http.csrf((auth) -> auth.disable());

        //form 로그인 방식 비활성화
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 비활성화
        http.httpBasic((auth) -> auth.disable());

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated());

        // JWT 검증 필터 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션(상태 유지) 비활성화
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
