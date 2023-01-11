package com.cocoball.kakaoapipractice.api.service


import spock.lang.Specification

import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService;

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService(); // 인스턴스화
    }

    def "buildUriByAddressSearch - 한글 파라미터의 경우 정상적으로 인코딩한다"() {
        given:
        String address = "서울 성북구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriAddressSearch(address)
        def decodedResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodedResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"
    }

}
