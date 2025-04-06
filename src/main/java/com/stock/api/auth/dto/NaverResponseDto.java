package com.stock.api.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverResponseDto {

    private String resultcode;

    private String message;

    private NaverUserDto response;
}