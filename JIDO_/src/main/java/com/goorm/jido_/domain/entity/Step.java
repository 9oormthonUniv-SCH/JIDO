package com.goorm.jido_.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 스텝 엔티티 클래스
 */
@Entity
@Table(name = "step")
@Getter
@Setter
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long stepId; // 스텝 고유 ID

    @ManyToOne
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap; // 소속 로드맵

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private RoadmapSection roadmapSection;

    @Column(name = "title")
    private String title; // 스텝 제목

    @Column(name = "sequence")
    private Integer sequence; // 순서

    @OneToMany(mappedBy = "step")
    private List<StepContent> contents; // 해당 스텝의 컨텐츠들
}