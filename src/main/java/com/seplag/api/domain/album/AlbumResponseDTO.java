package com.seplag.api.domain.album;

import java.time.Instant;
import java.util.UUID;

public record AlbumResponseDTO(UUID id,
                               String nomealbum,
                               String anoLancamento,
                               String imgUrl,
                               Instant criadoEm) {
}
