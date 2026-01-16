package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.ArtistaRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {
    public Album createAlbum(AlbumRequestDTO data) {
        String imgUrl = null;
        if (data.imgUrl() != null) {
            imgUrl = this.uploadImg(data.imgUrl());

        }
        Album newalbum = new Album();
        newalbum.setNomeAlbum(data.nomealbum());
        newalbum.setAnoLancamento(data.anoLancamento());
        newalbum.setImgUrl(imgUrl);

        return newalbum;
    }

    private String uploadImg(MultipartFile multipartFile) {
        return "";
    }
}
