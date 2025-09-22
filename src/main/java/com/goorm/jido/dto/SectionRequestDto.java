package com.goorm.jido.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SectionRequestDto(
        @NotBlank(message = "section title must not be blank") String title,
        String description,
        List<StepRequestDto> steps
) {}
