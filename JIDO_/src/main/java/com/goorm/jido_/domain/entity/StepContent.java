package com.goorm.jido_.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 스텝 컨텐츠 엔티티 클래스
 */
@Entity
@Table(name = "step_content")
@Getter
@Setter
public class StepContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId; // 컨텐츠 고유 ID

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    private Step step; // 소속 스텝

    @Column(name = "content_type")
    private String contentType; // 컨텐츠 타입 (예: TEXT, VIDEO 등)

    @Column(name = "content_value")
    private String contentValue; // 실제 컨텐츠 값

    @Column(name = "sequence")
    private Integer sequence; // 컨텐츠 순서
}