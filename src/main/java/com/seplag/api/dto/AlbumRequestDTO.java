package com.seplag.api.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;
public record AlbumRequestDTO(
        String nomealbum,
        String anoLancamento,
        Set<UUID> artistaIds
) {}
