package com.seplag.api.dto;

public record RegionalResponseDTO(
        Long id,
        Integer idExterno,
        String nome,
        Boolean ativo
) {
}
