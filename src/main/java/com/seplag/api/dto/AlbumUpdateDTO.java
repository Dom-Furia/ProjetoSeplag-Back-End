package com.seplag.api.dto;

import org.springframework.web.multipart.MultipartFile;

public record AlbumUpdateDTO(
        String nomealbum,
        String anoLancamento,
        MultipartFile imgUrl
) {

}
