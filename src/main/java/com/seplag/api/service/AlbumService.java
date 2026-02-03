package com.seplag.api.service;

import com.seplag.api.controller.WebSocketNotificationController;
import com.seplag.api.domain.album.Album;
import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumUpdateDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AlbumService {
    private final MinioStorageService minioStorageService;
    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final WebSocketNotificationController webSocketNotificationController;

    //Construtor
    public AlbumService(MinioStorageService minioStorage,
                        AlbumRepository albumRepository,
                        ArtistaRepository artistaRepository, WebSocketNotificationController webSocketNotificationController)
    {
        this.minioStorageService = minioStorage;
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.webSocketNotificationController = webSocketNotificationController;
    }

    @Transactional
    public Album createAlbumV1(AlbumRequestDTO data) {
        if (data.nomealbum() == null || data.nomealbum().isBlank()) {
            throw new IllegalArgumentException("O nome do álbum é obrigatório");
        }

        if (data.anoLancamento() == null || data.anoLancamento().isBlank()) {
            throw new IllegalArgumentException("O ano de lançamento é obrigatório");
        }

        if (data.artistaIds() == null || data.artistaIds().isEmpty()) {
            throw new IllegalArgumentException("Deve ter pelo menos um artista");
        }

        Set<Artista> artistas = new HashSet<>(artistaRepository.findAllById(data.artistaIds()));
        if (artistas.size() != data.artistaIds().size()) {
            throw new EntityNotFoundException("Alguns artistas não foram encontrados");
        }

        String imgUrl = null;
        MultipartFile file = data.imgUrl();
        if (file != null && !file.isEmpty()) {
            imgUrl = this.uploadImg(file);
        }

        Album newAlbum = new Album();
        newAlbum.setNomeAlbum(data.nomealbum());
        newAlbum.setAnoLancamento(data.anoLancamento());
        newAlbum.setImgUrl(imgUrl);
        newAlbum.setArtistas(artistas);

        Album savedAlbum = albumRepository.save(newAlbum);

        webSocketNotificationController.notifyNewAlbum(savedAlbum);

        return savedAlbum;
    }


    @Transactional(readOnly = true)
    public List<Album> getAllAlbumsV1(int page,
                                      int pageSize,
                                      String nomeArtista,
                                      Sort.Direction order) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(order, "nomeAlbum"));
        if (nomeArtista == null || nomeArtista.isBlank()) {
            return albumRepository.findAll(pageable).getContent();
        } else {
            return albumRepository.findByArtistaNome(nomeArtista, pageable).getContent();
        }
    }

    @Transactional
    public List<Album> getAllAlbumsV2(
            int page, int pageSize,
            String nomeArtista,
            String tipo,
            Sort.Direction order) {

        TipoArtista tipoEnum = null;
        if (tipo != null && !tipo.isBlank()) {
            tipoEnum = TipoArtista.valueOf(tipo.toUpperCase());
        }
        String nomeFiltro = (nomeArtista == null || nomeArtista.isBlank()) ? "" : nomeArtista;

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(order, "nomeAlbum"));
        return albumRepository.findByArtistaNomeAndTipo(nomeFiltro, tipoEnum, pageable).getContent();
    }


    @Transactional
    public void  deleteByIdV1(UUID id) {
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("Álbum não encontrado");
        }
        albumRepository.deleteById(id);
    }

    @Transactional
    public Album updatePartialV1(UUID id, AlbumUpdateDTO dto) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        if (dto.nomealbum() != null) {
            album.setNomeAlbum(dto.nomealbum());
        }

        if (dto.anoLancamento() != null) {
            album.setAnoLancamento(dto.anoLancamento());
        }

        if (dto.imgUrl() != null && !dto.imgUrl().isEmpty()) {
            String novaUrl = minioStorageService.upload(dto.imgUrl());
            album.setImgUrl(novaUrl);
        }

        return albumRepository.save(album);
    }

    @Transactional
    public Album updateV1(UUID id, AlbumUpdateDTO dto) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        album.setNomeAlbum(dto.nomealbum());
        album.setAnoLancamento(dto.anoLancamento());
        String novaUrl = minioStorageService.upload(dto.imgUrl());
        album.setImgUrl(novaUrl);

        return albumRepository.save(album);
    }

    private String uploadImg(MultipartFile multipartFile) {

        return minioStorageService.upload(multipartFile);
    }
}
