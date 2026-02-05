package com.seplag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ArtistaRequestDTO(
        @Schema(description = "Nome", example = "Elton John")
        String nome,

        @Schema(description = "Nacionalidade", example = "Americano")
        String nacionalidade,

        @Schema(description = "Tipo", example = "CANTOR")
        String tipo
) {
}
