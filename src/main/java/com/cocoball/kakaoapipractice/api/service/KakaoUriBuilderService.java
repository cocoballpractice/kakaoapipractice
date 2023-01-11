package com.cocoball.kakaoapipractice.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoUriBuilderService {

    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    // URI를 생성해주는 메서드
    public URI buildUriAddressSearch(String address) {

        /**
         * 기본 URL + query=에 검색할 실제 주소를 부착해야 함
         */

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
        uriBuilder.queryParam("query", address);

        URI uri = uriBuilder.build().encode().toUri(); // UTF-8로 인코딩해줌
        log.info("[KakaoUriBuilderService buildUriAddressSearch] address {}, uri: {}", address, uri);

        return uri;

    }

}
