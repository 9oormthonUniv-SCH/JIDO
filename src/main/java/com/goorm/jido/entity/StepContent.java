package com.goorm.jido.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goorm.jido.dto.StepContentRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "step_content")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StepContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_content_id")
    private Long stepContentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    @JsonIgnore                             // 역참조 직렬화 방지
    @OnDelete(action = OnDeleteAction.CASCADE) // DB 레벨 연쇄삭제 힌트
    private Step step;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    void assignStep(Step step) {
        this.step = step;
    }

    public void update(String content, Boolean finished) {
        if (content != null) this.content = content;
        if (finished != null) this.finished = finished;
    }
    public static StepContent fromDto(StepContentRequestDto dto, Step step) {
        return StepContent.builder()
                .content(dto.value())
                .step(step)
                .build();
    }

}
