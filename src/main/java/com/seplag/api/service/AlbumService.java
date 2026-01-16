package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {
    private final MinioStorageService minioStorageService;

    public AlbumService(MinioStorageService minioStorage) {
        this.minioStorageService = minioStorage;
    }

    public Album createAlbum(AlbumRequestDTO data) {
        String imgUrl = null;

        if (data.imgUrl() != null && !data.imgUrl().isEmpty()) {
            imgUrl = this.uploadImg(data.imgUrl());

        }
        Album newAlbum = new Album();
        newAlbum.setNomeAlbum(data.nomealbum());
        newAlbum.setAnoLancamento(data.anoLancamento());
        newAlbum.setImgUrl(imgUrl);

        return newAlbum;
    }

    private String uploadImg(MultipartFile multipartFile) {
        return minioStorageService.upload(multipartFile);
    }
}
