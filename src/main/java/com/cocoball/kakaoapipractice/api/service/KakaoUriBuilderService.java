package com.cocoball.kakaoapipractice.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoUriBuilderService {

    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    private static final String KAKAO_LOCAL_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";


    // URI를 생성해주는 메서드, KakaoAddressSearchService용
    public URI buildUriByAddressSearch(String address) {

        /**
         * 기본 URL + query=에 검색할 실제 주소를 부착해야 함
         */

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriBuilder.queryParam("query", address);

        URI uri = uriBuilder.build().encode().toUri(); // UTF-8로 인코딩해줌
        log.info("[KakaoUriBuilderService buildUriByAddressSearch] address {}, uri: {}", address, uri);

        return uri;

    }

    // URI를 생성해주는 메서드, KakaoCategorySearchService용
    public URI buildUriByCategorySearch(double latitude, double longitude, double radius, String category) {

        /**
         * 사용자의 위도, 경도 기준 가까운 장소 추적
         */

        double meterRadius = radius * 1000;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_CATEGORY_SEARCH_URL);
        uriBuilder.queryParam("category_group_code", category);
        uriBuilder.queryParam("x", longitude);
        uriBuilder.queryParam("y", latitude);
        uriBuilder.queryParam("radius", meterRadius);
        uriBuilder.queryParam("sort", "distance");

        URI uri = uriBuilder.build().encode().toUri(); // UTF-8로 인코딩해줌
        log.info("[KakaoUriBuilderService buildUriByCategorySearch] uri: {}", uri);

        return uri;

    }

}
