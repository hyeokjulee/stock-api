package com.stock.api.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtAccessTokenDto {

    private String accessToken;
}
