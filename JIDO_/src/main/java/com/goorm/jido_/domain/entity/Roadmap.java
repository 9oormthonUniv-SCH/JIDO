package com.goorm.jido_.domain.entity;

import com.goorm.jido_.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 로드맵 엔티티 클래스
 */
@Entity
@Table(name = "roadmap")
@Getter
@Setter
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long roadmapId; // 로드맵 고유 ID

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 작성자 (User와 연관)

    @Column(name = "title")
    private String title; // 로드맵 제목

    @Column(name = "description")
    private String description; // 설명

    @Column(name = "category")
    private String category; // 카테고리

    @Column(name = "is_public")
    private Boolean isPublic = true; // 공개 여부 (기본값 true)

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt; // 생성 일시

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt; // 수정 일시

    @OneToMany(mappedBy = "roadmap")
    private List<Step> steps; // 로드맵에 속한 스텝 리스트
}