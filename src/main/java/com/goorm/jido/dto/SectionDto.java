package com.goorm.jido.dto;

import com.goorm.jido.entity.RoadmapSection;
import com.goorm.jido.entity.Step;
import java.util.Comparator;
import java.util.List;

public record SectionDto(
        Long sectionId,
        String title,
        String description,
        Long sectionNum,
        List<StepDto> steps
) {
    public static SectionDto of(RoadmapSection s, List<StepDto> steps) {
        return new SectionDto(
                s.getSectionId(),
                s.getTitle(),
                s.getDescription(),
                s.getSectionNum(),
                steps
        );
    }
}

