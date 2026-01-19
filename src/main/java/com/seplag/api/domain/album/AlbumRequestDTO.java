package com.seplag.api.domain.album;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record AlbumRequestDTO(String nomealbum, String anoLancamento, MultipartFile imgUrl, UUID artistaId) {
}
