package com.goorm.jido_.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로드맵 생성 요청을 받을 DTO 클래스
 * DB의 Roadmap 테이블 컬럼명 및 타입을 반영하여 작성됨
 */
@Getter
@Setter
public class RoadmapRequestDto {

    // foreign key - User 테이블 참조
    private Long authorId;

    private String title;

    private String description;

    private String category;

    private Boolean isPublic;

    // created_at, updated_at은 자동 생성되므로 입력받지 않음
}
