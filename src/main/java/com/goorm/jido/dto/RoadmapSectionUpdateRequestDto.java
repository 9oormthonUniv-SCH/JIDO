// RoadmapSectionUpdateRequestDto.java
package com.goorm.jido.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoadmapSectionUpdateRequestDto(
        String title,
        String description,
        Long sectionNum
) {}
