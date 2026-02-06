package com.seplag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AlbumUpdateDTO(
        @Schema(description = "Nome do album", example = "Cintilante")
        String nomealbum,

        @Schema(description = "Ano de Lançamento", example = "2023")
        String anoLancamento
) {
}
