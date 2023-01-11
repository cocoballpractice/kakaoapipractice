package com.cocoball.kakaoapipractice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDto {

    /**
     * Kakao 로컬/지도의 Meta 부분 Dto
     */

    @JsonProperty("total_count") // json으로 응답을 보낼 때의 필드명
    private Integer totalCount;

}
