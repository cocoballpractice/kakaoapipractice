package com.cocoball.kakaoapipractice.pharmacy.service;

import com.cocoball.kakaoapipractice.pharmacy.cache.PharmacyRedisTemplateService;
import com.cocoball.kakaoapipractice.pharmacy.dto.PharmacyDto;
import com.cocoball.kakaoapipractice.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList() {

        // REDIS
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if(!pharmacyDtoList.isEmpty()) {
            log.info("redis findAll success!");
            return pharmacyDtoList; // RedisTemplate에서 장애 발생 시 빈 리스트 출력
        }

        // DB
        return pharmacyRepositoryService.findAll()
                .stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
    }


    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {

        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }

}
