package com.seplag.api.dto;

import java.time.Instant;
import java.util.UUID;

public record CapaAlbumResponseDTO(
        UUID id,
        String objectName,
        Instant criadoEm
) {
}
