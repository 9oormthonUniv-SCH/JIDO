package com.goorm.jido.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goorm.jido.dto.SectionRequestDto;
import com.goorm.jido.dto.StepRequestDto;
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

    // ✅ 새로 추가
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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

    public void update(String title, String description, Long sectionNum) {
        if (title != null && !title.isBlank()) this.title = title;
        if (description != null) this.description = description;
        if (sectionNum != null) this.sectionNum = sectionNum;
    }

    public static RoadmapSection fromDto(SectionRequestDto dto, Roadmap roadmap, Long sectionNum) {
        RoadmapSection section = RoadmapSection.builder()
                .title(dto.title())
                .description(dto.description())
                .sectionNum(sectionNum)
                .roadmap(roadmap)
                .build();

        // ✅ StepRequestDto → Step 변환
        if (dto.steps() != null) {
            long order = 1L;
            for (StepRequestDto stepDto : dto.steps()) {
                Step step = Step.fromDto(stepDto, section, order++);
                section.addStep(step);
            }
        }

        return section;
    }
}

