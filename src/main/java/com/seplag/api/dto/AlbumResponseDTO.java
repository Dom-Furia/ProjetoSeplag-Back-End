package com.seplag.api.dto;

import java.time.Instant;
import java.util.UUID;

public record AlbumResponseDTO(UUID id,
                               String nomealbum,
                               String anoLancamento,
                               String imgUrl,
                               Instant criadoEm) {
}
