package com.cocoball.kakaoapipractice.pharmacy.controller;

import com.cocoball.kakaoapipractice.pharmacy.cache.PharmacyRedisTemplateService;
import com.cocoball.kakaoapipractice.pharmacy.dto.PharmacyDto;
import com.cocoball.kakaoapipractice.pharmacy.service.PharmacyRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    /**
     * REDIS와 DB의 데이터 동기화를 위한 컨트롤러
     */

    // 데이터 초기 세팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {

        List<PharmacyDto> pharmacyDtoList = pharmacyRepositoryService.findAll()
                .stream().map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .collect(Collectors.toList());

        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save); // REDIS로 저장

        return "success";
    }
}
