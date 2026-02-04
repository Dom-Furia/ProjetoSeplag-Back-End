package com.seplag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;
public record AlbumRequestDTO(
        @Schema(
                description = "Nome do álbum",
                example = "Hybrid Theory"
        )
        @NotBlank
        String nomealbum,

        @Schema(
                description = "Ano de lançamento",
                example = "2000"
        )
        @NotBlank
        String anoLancamento,

        @Schema(
                description = "IDs dos artistas vinculados ao álbum",
                example = "[\"550e8400-e29b-41d4-a716-446655440000\"]"
        )
        @NotEmpty
        Set<UUID> artistaId

) {}
