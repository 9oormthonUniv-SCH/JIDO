package com.goorm.jido.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record StepRequestDto(
        @NotBlank(message = "step title must not be blank") String title,
        Long stepNumber,
        List<StepContentRequestDto> contents
) {}