package com.stock.api.service;

import com.stock.api.dto.JwtDto;
import com.stock.api.dto.NaverResponseDto;
import com.stock.api.entity.User;
import com.stock.api.repository.UserRepository;
import com.stock.api.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestClient restClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JwtDto login(String naverAccessToken) {
        // 네이버 사용자 정보를 가져오기 위한 API 호출
        String naverApiUrl = "https://openapi.naver.com/v1/nid/me";
        NaverResponseDto naverResponseDto = fetchNaverUserInfo(naverApiUrl, naverAccessToken);

        // 사용자 이메일과 이름 추출
        String email = naverResponseDto.getResponse().getEmail();
        String name = naverResponseDto.getResponse().getName();

        // DB에 사용자 정보가 없으면 자동으로 회원가입 처리
        registerUserIfNotExists(email, name);

        // JWT 생성
        String token = jwtUtil.generateToken(email, name);
        JwtDto jwtDto = new JwtDto(token);

        return jwtDto;
    }

    private NaverResponseDto fetchNaverUserInfo(String naverApiUrl, String naverAccessToken) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + naverAccessToken);

            return restClient.method(HttpMethod.GET)
                    .uri(naverApiUrl)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .toEntity(NaverResponseDto.class) // 응답 본문을 NaverResponseDto으로 변환
                    .getBody();
    }

    private void registerUserIfNotExists(String email, String name) {
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(new User(email, name));
        }
    }
}