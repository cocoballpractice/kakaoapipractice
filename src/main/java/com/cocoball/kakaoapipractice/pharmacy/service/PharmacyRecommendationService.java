package com.cocoball.kakaoapipractice.pharmacy.service;

import com.cocoball.kakaoapipractice.api.dto.DocumentDto;
import com.cocoball.kakaoapipractice.api.dto.KakaoApiResponseDto;
import com.cocoball.kakaoapipractice.api.service.KakaoAddressSearchService;
import com.cocoball.kakaoapipractice.direction.dto.OutputDto;
import com.cocoball.kakaoapipractice.direction.entity.Direction;
import com.cocoball.kakaoapipractice.direction.service.Base62Service;
import com.cocoball.kakaoapipractice.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview";

    @Value("${pharmacy.recommendation.base.url")
    private String baseUrl;

    public List<OutputDto> recommendationPharmacyList(String address) {

        // 문자열 기반 주소로 카카오 로컬/지도 검색 API 호출
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendationPharmacyList fail] input address : {}", address);
            return Collections.emptyList();
        }

        // API 호출 결과를 Dto로 가져오기 (위치 기반 데이터)
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // Dto를 목적지 리스트로 변환(거리 계산 포함)
        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        // 목적지 리스트를 저장하고 Dto 리스트 반환
        return directionService.saveAll(directionList)
                .stream()
                .map(t -> convertToOutputDto(t))
                .collect(Collectors.toList());
    }

    public void recommendationPharmacyListByApi(String address) {

        // 문자열 기반 주소로 카카오 로컬/지도 검색 API 호출
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendationPharmacyList fail] input address : {}", address);
            return;
        }

        // API 호출 결과를 Dto로 가져오기 (위치 기반 데이터)
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // Dto를 목적지 리스트로 변환(거리 계산 포함)
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        // 목적지 리스트를 저장
        directionService.saveAll(directionList);

    }

    private OutputDto convertToOutputDto(Direction direction) {

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }

}
