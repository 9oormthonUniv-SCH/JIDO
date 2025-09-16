package com.goorm.jido.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goorm.jido.dto.SectionRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roadmap_section")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoadmapSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @JsonIgnore                                  // ✅ 직렬화 무한 루프 방지
    @OnDelete(action = OnDeleteAction.CASCADE)   // ✅ 부모 삭제 시 DB 레벨에서 함께 삭제
    private Roadmap roadmap;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "section_num", nullable = false)
    private Long sectionNum;

    @Builder.Default
    @OneToMany(mappedBy = "roadmapSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<Step> steps = new ArrayList<>();

    // ====== 편의 메서드 ======
    public void assignRoadmap(Roadmap roadmap) {     // 🚀 Service 계층에서 연관 주입용
        this.roadmap = roadmap;
    }

    public void addStep(Step step) {
        if (step == null) return;
        step.assignSection(this);
        this.steps.add(step);
    }

    public void removeStep(Step step) {
        if (step == null) return;
        this.steps.remove(step);
        step.clearSection();
    }

    public void update(String title, Long sectionNum) {
        if (title != null && !title.isBlank()) this.title = title;
        if (sectionNum != null) this.sectionNum = sectionNum;
    }

    // ====== DTO 변환 메서드 ======
    public static RoadmapSection fromDto(SectionRequestDto dto, Roadmap roadmap, Long sectionNum) {
        return RoadmapSection.builder()
                .title(dto.getTitle())
                .sectionNum(sectionNum)   // 🚀 순서 번호 Service에서 매겨줄 수 있음
                .roadmap(roadmap)
                .build();
    }
}
