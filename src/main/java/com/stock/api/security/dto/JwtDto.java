package com.stock.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtDto {

    private String accessToken;

    private String refreshToken;
}
