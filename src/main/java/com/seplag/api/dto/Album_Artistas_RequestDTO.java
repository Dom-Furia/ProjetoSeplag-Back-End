package com.seplag.api.dto;

import java.util.Set;
import java.util.UUID;

public record Album_Artistas_RequestDTO(
        UUID albumId,
        Set<UUID> artistasIds
) {
}
