package com.cocoball.kakaoapipractice.pharmacy.cache;

import com.cocoball.kakaoapipractice.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

    private static final String CACHE_KEY = "PHARMACY";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper; // objectMapper를 통해 serializaion, deserialization 진행

    private HashOperations<String, String, String> hashOperations;
    // CACHE_KEY, SUBKEY(약국 PK값), VALUE(약국 DTO -> ObjectMapper로 JSON 형태로 변경하여 저장)

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash(); // redisTemplate 빈 등록 시 hashOperations 빈 생성
    }

    public void save(PharmacyDto pharmacyDto) {
        if(Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.getId().toString(),
                    serializePharmacyDto(pharmacyDto));
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.getId());
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }

    }

    public List<PharmacyDto> findAll() {

        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                PharmacyDto pharmacyDto = deserializePharmacyDto(value);
                list.add(pharmacyDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService findAll error] {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete] id: {}", id);
    }


    // Dto -> JSON
    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pharmacyDto); // writeValueAsString : 객체를 JSON 형태로 저장
    }

    // JSON -> Dto
    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, PharmacyDto.class); // readValue : 매개변수를 class의 필드에 맞게 변경경
   }

}

