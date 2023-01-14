package com.cocoball.kakaoapipractice.api.service;

import com.cocoball.kakaoapipractice.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate; // config에서 Bean으로 생성하여 의존성 주입 완료
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Retryable(
            value = {RuntimeException.class}, // RuntimeException 발생 시
            maxAttempts = 2, // 초기 요청 포함 2회 재요청
            backoff = @Backoff(delay = 2000) // 딜레이 2초
    )
    public KakaoApiResponseDto requestAddressSearch(String address) {

        // validation
        if(ObjectUtils.isEmpty(address)) return null;

        // URI 호출
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        // 헤더값 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        // Kakao API 호출
        // exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) throws RestClientException
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
    }

    @Recover // Retry 실패 시 처리 메서드, 주의점 : 원래 메서드의 반환 타입을 반환해야 함
    public KakaoApiResponseDto recover(RuntimeException e, String address) {
        log.error("모든 에러가 실패하였음. address: {}, error: {}", address, e.getMessage());
        return null;
    }

}
