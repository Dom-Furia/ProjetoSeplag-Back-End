package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.album.AlbumResponseDTO;
import com.seplag.api.domain.album.AlbumUpdateDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.lang.model.type.UnknownTypeException;
import java.util.List;
import java.util.UUID;

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

    @Transactional
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

    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> getAllAlbums(
            int page,
            int pageSize,
            TipoArtista tipo,
            String nomeArtista
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Album> albumPage;

        if (tipo != null && nomeArtista != null) {
            albumPage = albumRepository
                    .findByArtista_TipoAndArtista_NomeContainingIgnoreCase(
                            tipo, nomeArtista, pageable
                    );
        } else if (tipo != null) {
            albumPage = albumRepository.findByArtista_Tipo(tipo, pageable);
        } else {
            albumPage = albumRepository.findAll(pageable);
        }


        return albumPage.map(album -> new AlbumResponseDTO(
                album.getId(),
                album.getNomeAlbum(),
                album.getAnoLancamento(),
                album.getImgUrl(),
                album.getCriadoEm(),
                album.getArtista().getId(),
                album.getArtista().getNome())
        ).stream().toList();
    }

    @Transactional
    public void  deleteById(UUID id) {
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("Álbum não encontrado");
        }
        albumRepository.deleteById(id);
    }

    @Transactional
    public Album updatePartial(UUID id, AlbumUpdateDTO dto) {

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


    private String uploadImg(MultipartFile multipartFile) {
        return minioStorageService.upload(multipartFile);
    }
}
