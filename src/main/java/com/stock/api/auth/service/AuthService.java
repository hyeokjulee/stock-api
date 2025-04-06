package com.stock.api.auth.service;

import com.stock.api.auth.dto.NaverResponseDto;
import com.stock.api.security.dto.JwtDto;
import com.stock.api.security.service.JwtService;
import com.stock.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RestClient restClient;
    private final UserService userService;
    private final JwtService jwtService;

    public JwtDto login(String naverAccessToken) {

        // 네이버 사용자 정보를 가져오기 위한 API 호출
        String naverApiUrl = "https://openapi.naver.com/v1/nid/me";
        NaverResponseDto naverResponseDto = fetchNaverUserInfo(naverApiUrl, naverAccessToken);

        String email = naverResponseDto.getResponse().getEmail();
        String name = naverResponseDto.getResponse().getName();

        userService.registerUserIfNotExists(email, name);

        JwtDto jwtDto = jwtService.createJwt(email, name);

        return jwtDto;
    }

    public JwtDto refresh(String refreshToken) {

        JwtDto jwtDto = jwtService.refreshJwt(refreshToken);

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
}