package com.seplag.api.dto;

import java.util.Set;
import java.util.UUID;

public record AlbumResponseDTO(
        UUID id,
        String nomeAlbum,
        String anoLancamento,
        Set<ArtistaResponseDTO> artistas
) {}
