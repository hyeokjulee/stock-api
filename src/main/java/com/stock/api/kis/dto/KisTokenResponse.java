package com.stock.api.kis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KisTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
