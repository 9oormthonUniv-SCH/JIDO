package com.goorm.jido_.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "roadmap_section")
@Getter
@Setter
public class RoadmapSection {

    // 섹션 ID (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    // 연결된 로드맵
    @ManyToOne
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;

    // 섹션 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 섹션 순서 (int형)
    @Column(name = "section_num")
    private Integer sectionNum;

    // 해당 섹션에 포함된 Step 리스트
    @OneToMany(mappedBy = "roadmapSection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps;
}
