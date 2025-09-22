package com.goorm.jido.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoadmapUpdateRequestDto(
        @Size(min = 1, message = "title must not be empty")
        String title,
        String description,
        String category,
        Boolean isPublic,

        List<@Valid SectionRequestDto> sections
) {}
