package com.stock.api.service;

import com.stock.api.dto.NaverResponseDto;
import com.stock.api.dto.NaverUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestClient restClient;

    public NaverUserDto fetchNaverUserInfo(String naverAccessToken) {
        
        String url = "https://openapi.naver.com/v1/nid/me"; // 네이버 API URL

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + naverAccessToken);

        // GET 요청 보내기
        NaverResponseDto naverResponseDto = restClient.method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve() // 응답을 받을 준비
                .toEntity(NaverResponseDto.class) // 응답 본문을 String으로 변환
                .getBody();

        NaverUserDto naverUserDto = naverResponseDto.getResponse();

        return naverUserDto;
    }
}
