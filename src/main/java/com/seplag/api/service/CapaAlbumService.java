package com.seplag.api.service;


import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.imagem_capa.CapaAlbum;
import com.seplag.api.dto.CapaAlbumResponseDTO;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.CapaAlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CapaAlbumService {

    private final AlbumRepository albumRepository;
    private final CapaAlbumRepository capaAlbumRepository;
    private final MinioStorageService minioService;

    public CapaAlbumService(AlbumRepository albumRepository, CapaAlbumRepository capaAlbumRepository, MinioStorageService minioService) {
        this.albumRepository = albumRepository;
        this.capaAlbumRepository = capaAlbumRepository;
        this.minioService = minioService;
    }

    /* --------------------------CRIAR --------------------- */
    @Transactional
    public CapaAlbumResponseDTO criar(UUID albumId, MultipartFile file) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        validarImagem(file);

        String objectName = minioService.upload(file);

        CapaAlbum capa = new CapaAlbum();
        capa.setObjectName(objectName);
        capa.setAlbum(album);

        capaAlbumRepository.save(capa);

        return toDTO(capa);
    }

    /* -------------------------- LISTAR --------------------- */
    @Transactional
    public List<CapaAlbumResponseDTO> listarPorAlbum(UUID albumId) {

        return capaAlbumRepository.findByAlbumId(albumId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* --------------------------BUSCAR POR ID --------------------- */
    @Transactional
    public CapaAlbumResponseDTO buscarPorId(UUID capaId) {

        CapaAlbum capa = capaAlbumRepository.findById(capaId)
                .orElseThrow(() -> new RuntimeException("Capa não encontrada"));

        return toDTO(capa);
    }



    public String download(UUID capaId) {

        CapaAlbum capa = capaAlbumRepository.findById(capaId)
                .orElseThrow(() -> new RuntimeException("Capa não encontrada"));

        return minioService.generateUrl(capa.getObjectName());
    }

    /* --------------------------UPDATE --------------------- */

    @Transactional
    public CapaAlbumResponseDTO atualizar(UUID capaId, MultipartFile novaImagem) {

        CapaAlbum capa = capaAlbumRepository.findById(capaId)
                .orElseThrow(() -> new RuntimeException("Capa não encontrada"));

        validarImagem(novaImagem);

        // remove imagem antiga
        minioService.delete(capa.getObjectName());

        // upload nova
        String novoObjectName = minioService.upload(novaImagem);
        capa.setObjectName(novoObjectName);

        return toDTO(capa);
    }

    /* -------------------------- DELETE -------------------------- */

    @Transactional
    public void deletar(UUID capaId) {

        CapaAlbum capa = capaAlbumRepository.findById(capaId)
                .orElseThrow(() -> new RuntimeException("Capa não encontrada"));

        minioService.delete(capa.getObjectName());
        capaAlbumRepository.delete(capa);
    }


    private void validarImagem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo inválido");
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new RuntimeException("Arquivo não é uma imagem");
        }
    }

    private CapaAlbumResponseDTO toDTO(CapaAlbum capa) {
        return new CapaAlbumResponseDTO(
                capa.getId(),
                capa.getObjectName(),
                capa.getCriadoEm()
        );
    }


}
