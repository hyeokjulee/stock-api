package com.stock.api.kis.client;

import com.stock.api.kis.dto.KisStocksResponse;
import com.stock.api.kis.dto.KisTokenRequest;
import com.stock.api.kis.dto.KisTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KisApiClient {

    private final RestClient restClient;
    private final String appKey;
    private final String appSecret;
    private final KisTokenRequest kisTokenRequest;

    private static final String DOMAIN = "https://openapi.koreainvestment.com:9443";

    public KisApiClient(RestClient restClient,
                        @Value("${kis.app-key}") String appKey,
                        @Value("${kis.app-secret}") String appSecret) {
        this.restClient = restClient;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.kisTokenRequest = new KisTokenRequest("client_credentials", appKey, appSecret);
    }

    public KisStocksResponse fetchMarketCapRanking(String exchangeCode, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json; charset=utf-8"); // 컨텐츠타입
        headers.set("authorization", "Bearer " + accessToken); // 접근토큰
        headers.set("appkey", appKey); // 앱키
        headers.set("appsecret", appSecret); // 앱시크릿키
        headers.set("tr_id", "HHDFS76350100"); // 거래ID
        headers.set("custtype", "P"); // 고객 타입

        String uri = UriComponentsBuilder.fromHttpUrl(DOMAIN + "/uapi/overseas-stock/v1/ranking/market-cap")
                .queryParam("AUTH", "") // 사용자권한정보
                .queryParam("EXCD", exchangeCode) // 거래소코드
                .queryParam("VOL_RANG", 0) // 거래량조건
                .queryParam("KEYB", "") // NEXT KEY BUFF
                .toUriString();

        return restClient.method(HttpMethod.GET)
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(KisStocksResponse.class) // 응답 본문을 KisStocksResponse로 변환
                .getBody();
    }

    public KisTokenResponse fetchAccessToken() {

        return restClient.method(HttpMethod.POST)
                .uri(DOMAIN + "/oauth2/tokenP")
                .body(kisTokenRequest)
                .retrieve()
                .toEntity(KisTokenResponse.class) // 응답 본문을 KisTokenResponse로 변환
                .getBody();
    }
}
