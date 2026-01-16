package com.seplag.api.domain.album;

import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

public record AlbumRequestDTO(String nomealbum, String anoLancamento, MultipartFile imgUrl) {
}
