package com.stock.api.auth.service;

import com.stock.api.auth.dto.NaverUserResponse;
import com.stock.api.auth.dto.NaverUserInfoDto;
import com.stock.api.auth.dto.JwtDto;
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

    private static final String NAVER_API_URL = "https://openapi.naver.com/v1/nid/me";

    public JwtDto login(String naverAccessToken) {

        // 네이버 사용자 정보를 가져오기 위한 API 호출
        NaverUserResponse naverUserResponse = fetchNaverUserInfo(naverAccessToken);

        NaverUserInfoDto naverUserInfoDto = naverUserResponse.getResponse();
        String email = naverUserInfoDto.getEmail();
        String name = naverUserInfoDto.getName();

        userService.registerUserIfNotExists(email, name);

        JwtDto jwtDto = jwtService.createJwt(email, name);

        return jwtDto;
    }

    public JwtDto refresh(String refreshToken) {

        return jwtService.refreshJwtFromRefreshToken(refreshToken);
    }

    public void logout(String refreshToken) {

        jwtService.logoutFromRefreshToken(refreshToken);
    }

    private NaverUserResponse fetchNaverUserInfo(String naverAccessToken) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + naverAccessToken);

            return restClient.method(HttpMethod.GET)
                    .uri(NAVER_API_URL)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .toEntity(NaverUserResponse.class) // 응답 본문을 NaverUserResponse로 변환
                    .getBody();
    }
}