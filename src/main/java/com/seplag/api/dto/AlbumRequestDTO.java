package com.seplag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;
public record AlbumRequestDTO(
        @Schema(description = "Nome do album", example = "Cintilante")
        String nomealbum,

        @Schema(description = "Ano de Lançamento", example = "2023")
        String anoLancamento,

        @Schema(description = "Nome do Artista", example = "Simone Mendes")
        Set<UUID> artistaIds
) {}
