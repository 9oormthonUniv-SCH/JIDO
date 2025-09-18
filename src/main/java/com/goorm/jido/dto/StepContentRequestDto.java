package com.goorm.jido.dto;

import jakarta.validation.constraints.NotBlank;

public record StepContentRequestDto(
        @NotBlank(message = "content type must not be blank") String contentType,
        @NotBlank(message = "content value must not be blank") String value
) {}
