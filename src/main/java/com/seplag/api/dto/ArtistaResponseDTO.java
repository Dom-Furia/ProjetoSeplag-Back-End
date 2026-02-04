package com.seplag.api.dto;

import java.util.UUID;

public record ArtistaResponseDTO(
        UUID id,
        String nome,
        String nacionalidade,
        String tipo) {
}
