package com.seplag.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequestDTO(
        @Schema(example = "João")
        String name,

        @Schema(example = "joao@test.com")
        String email,

        @Schema(example = "Test@2026")
        String password
) {
}
