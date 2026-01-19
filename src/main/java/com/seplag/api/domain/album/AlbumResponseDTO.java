package com.seplag.api.domain.album;

import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

public record AlbumResponseDTO(UUID id,
                               String nomealbum,
                               String anoLancamento,
                               String imgUrl,
                               Instant criadoEm,
                               UUID artistaId,
                               String artistaNome) {
}
