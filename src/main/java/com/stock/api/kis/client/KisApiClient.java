package com.stock.api.kis.client;

import com.stock.api.kis.dto.KisStocksResponse;
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

    private static final String Domain = "https://openapi.koreainvestment.com:9443";
    private static final String URL = "/uapi/overseas-stock/v1/ranking/market-cap";

    public KisApiClient(RestClient restClient,
                        @Value("${kis.app-key}") String appKey,
                        @Value("${kis.app-secret}") String appSecret) {
        this.restClient = restClient;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public KisStocksResponse fetchMarketCapRanking(String excd) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json; charset=utf-8"); // 컨텐츠타입
        headers.set("authorization", ""); // 접근토큰
        headers.set("appkey", appKey); // 앱키
        headers.set("appsecret", appSecret); // 앱시크릿키
        headers.set("tr_id", "HHDFS76350100"); // 거래ID
        headers.set("custtype", "P"); // 고객 타입

        String uri = UriComponentsBuilder.fromHttpUrl(Domain + URL)
                .queryParam("AUTH", "") // 사용자권한정보
                .queryParam("EXCD", excd) // 거래소코드
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
}
