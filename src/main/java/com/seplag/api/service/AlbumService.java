package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlbumService {
    private final MinioStorageService minioStorageService;
    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;


    public AlbumService(MinioStorageService minioStorage,
                        AlbumRepository albumRepository,
                        ArtistaRepository artistaRepository)
    {
        this.minioStorageService = minioStorage;
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
    }

    public Album createAlbum(AlbumRequestDTO data) {
        String imgUrl = null;

        if (data.imgUrl() != null && !data.imgUrl().isEmpty()) {
            imgUrl = this.uploadImg(data.imgUrl());

        }
        Artista artista = artistaRepository.findById(data.artistaId())
                .orElseThrow(() -> new RuntimeException("Artista não encontrada"));

        Album newAlbum = new Album();
        newAlbum.setNomeAlbum(data.nomealbum());
        newAlbum.setAnoLancamento(data.anoLancamento());
        newAlbum.setImgUrl(imgUrl);
        newAlbum.setArtista(artista);

        return albumRepository.save(newAlbum);
    }

    private String uploadImg(MultipartFile multipartFile) {
        return minioStorageService.upload(multipartFile);
    }
}
