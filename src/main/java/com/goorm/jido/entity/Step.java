package com.goorm.jido.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goorm.jido.dto.StepContentRequestDto;
import com.goorm.jido.dto.StepRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "step")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long stepId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @JsonIgnore                                  // 역참조 직렬화 방지
    @OnDelete(action = OnDeleteAction.CASCADE)   // DB 레벨 연쇄삭제 힌트
    private RoadmapSection roadmapSection;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "step_number", nullable = false)
    private Long stepNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<StepContent> stepContents = new ArrayList<>();

    // ====== 편의 메서드 (세터 대신 내부 전용) ======
    void assignSection(RoadmapSection section) {  // package-private
        this.roadmapSection = section;
    }
    void clearSection() {                         // package-private
        this.roadmapSection = null;
    }

    public void addContent(StepContent content) {
        if (content == null) return;
        content.assignStep(this); // StepContent에 assignStep 메서드도 필요
        this.stepContents.add(content);
    }

    public void update(String title, Long stepNumber) {
        if (title != null && !title.isBlank()) this.title = title;
        if (stepNumber != null) this.stepNumber = stepNumber;
    }

    public static Step fromDto(StepRequestDto dto, RoadmapSection section, Long stepNum) {
        Step step = Step.builder()
                .title(dto.title())
                .stepNumber(stepNum)
                .roadmapSection(section)
                .build();

        // StepContentRequestDto → StepContent 변환
        if (dto.contents() != null) {
            for (StepContentRequestDto contentDto : dto.contents()) {
                StepContent content = StepContent.fromDto(contentDto, step);
                step.addContent(content);
            }
        }

        return step;
    }

}
