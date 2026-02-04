package com.seplag.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequestDTO(
        @Schema(description = "Nome do usuário", example = "Julio Cesar")
        String name,

        @Schema(description = "Email do usuário", example = "julio@test.com")
        String email,

        @Schema(description = "Senha do usuário", example = "Test@2026")
        String password
) {
}
