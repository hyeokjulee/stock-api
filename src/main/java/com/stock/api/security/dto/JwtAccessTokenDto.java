package com.stock.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtAccessTokenDto {

    private String accessToken;
}
