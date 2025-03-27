package com.stock.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverResponseDto {
    private String resultcode;
    private String message;
    private NaverUserDto response;
}