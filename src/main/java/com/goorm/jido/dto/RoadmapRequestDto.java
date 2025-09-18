package com.goorm.jido.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import jakarta.validation.Valid;

/**
 * 로드맵 생성 요청 DTO
 * - sections: 선택값(초기 섹션 제목 배열). 보내지 않거나 빈 배열이면 섹션 없이 로드맵만 생성됨.
 * - isPublic: 생략 시 엔티티 기본값(true)
 * - authorId: 비로그인 테스트용(로그인 시 미사용)
 */
public record RoadmapRequestDto(
        Long authorId,
        @NotBlank(message = "title must not be blank")
        String title,
        @NotBlank(message = "description must not be blank")
        String description,
        @NotBlank(message = "category must not be blank")
        String category,
        Boolean isPublic,
        List<@Valid SectionRequestDto> sections
) {}
