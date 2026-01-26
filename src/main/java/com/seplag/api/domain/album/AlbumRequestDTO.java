package com.seplag.api.domain.album;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;
public record AlbumRequestDTO(String nomealbum,
                              String anoLancamento,
                              MultipartFile imgUrl,
                              Set<UUID> artistaIds ) {
}
