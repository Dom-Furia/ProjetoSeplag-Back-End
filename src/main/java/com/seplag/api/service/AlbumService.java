package com.seplag.api.service;

import com.seplag.api.controller.WebSocketNotificationController;
import com.seplag.api.domain.album.Album;
import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumResponseDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.AlbumUpdateDTO;
import com.seplag.api.dto.ArtistaResponseDTO;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final WebSocketNotificationController webSocketNotificationController;

    //Construtor
    public AlbumService(
                        AlbumRepository albumRepository,
                        ArtistaRepository artistaRepository, WebSocketNotificationController webSocketNotificationController)
    {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.webSocketNotificationController = webSocketNotificationController;
    }


    //---------------------------- Criar Album ------------------------//
    @Transactional
    public AlbumResponseDTO createAlbumV1(AlbumRequestDTO dto) {

        if (dto.nomealbum() == null || dto.nomealbum().isBlank()) {
            throw new IllegalArgumentException("O nome do álbum é obrigatório");
        }

        if (dto.anoLancamento() == null || dto.anoLancamento().isBlank()) {
            throw new IllegalArgumentException("O ano de lançamento é obrigatório");
        }

        if (dto.artistaIds() == null || dto.artistaIds().isEmpty()) {
            throw new IllegalArgumentException("Deve ter pelo menos um artista");
        }

        Set<Artista> artistas = new HashSet<>(artistaRepository.findAllById(dto.artistaIds()));
        if (artistas.size() != dto.artistaIds().size()) {
            throw new EntityNotFoundException("Alguns artistas não foram encontrados");
        }


        Album newAlbum = new Album();
        newAlbum.setNomeAlbum(dto.nomealbum());
        newAlbum.setAnoLancamento(dto.anoLancamento());
        newAlbum.setArtistas(artistas);

        Album savedAlbum = albumRepository.save(newAlbum);

        webSocketNotificationController.notifyNewAlbum(savedAlbum);

        return toResponseDTO(savedAlbum);
    }

    //---------------------------- Adicionar Artistas ao Album ------------------------//
    @Transactional
    public AlbumResponseDTO adicionarArtistas(
            UUID albumId,
            Set<UUID> artistasIds)
    {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        Set<Artista> novos = new HashSet<>(
                artistaRepository.findAllById(artistasIds)
        );

        album.getArtistas().addAll(novos);

        return toResponseDTO(album);
    }

    //---------------------------- Listar Albuns ------------------------//
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> getAllAlbunsV1(
            int page, int pageSize,
            String nomeArtista,
            String tipo,
            Sort.Direction order) {

        TipoArtista tipoEnum = null;
        if (tipo != null && !tipo.isBlank()) {
            tipoEnum = TipoArtista.valueOf(tipo.toUpperCase());
        }

        String nomeFiltro = (nomeArtista == null || nomeArtista.isBlank()) ? "" : nomeArtista;

        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by(order, "nomeAlbum")
        );
        Page<Album> pageResult =
                albumRepository.findByArtistaNomeAndTipo(
                        nomeFiltro,
                        tipoEnum,
                        pageable
                );

        return pageResult.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    //---------------------------- Excluir Album ------------------------//
    @Transactional
    public void  deleteByIdV1(UUID id) {
        if (!albumRepository.existsById(id)) {
            throw new EntityNotFoundException("Álbum não encontrado");
        }
        albumRepository.deleteById(id);
    }

    //---------------------------- Atualizar Album Parcial ------------------------//
    @Transactional
    public AlbumResponseDTO updatePartialV1(UUID id, AlbumUpdateDTO dto) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        if (dto.nomealbum() != null) {
            album.setNomeAlbum(dto.nomealbum());
        }

        if (dto.anoLancamento() != null) {
            album.setAnoLancamento(dto.anoLancamento());
        }

        return toResponseDTO(albumRepository.save(album)) ;
    }

    //---------------------------- Atualizar Album ------------------------//
    @Transactional
    public AlbumResponseDTO updateV1(UUID id, AlbumUpdateDTO dto) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        if (dto.nomealbum() == null || dto.nomealbum().isBlank()) {
            throw new IllegalArgumentException("O nome do artista deve ser informado");
        }

        if (dto.anoLancamento() == null || dto.anoLancamento().isBlank()) {
            throw new IllegalArgumentException("O ano de lançamento deve ser informado");
        }

        album.setNomeAlbum(dto.nomealbum());
        album.setAnoLancamento(dto.anoLancamento());

         return toResponseDTO(albumRepository.save(album));

    }


    // Converter um Album em AlbumResponseDTO com artistas
    private AlbumResponseDTO toResponseDTO(Album album) {
        return new AlbumResponseDTO(
                album.getId(),
                album.getNomeAlbum(),
                album.getAnoLancamento(),
                album.getArtistas().stream()
                        .map(a -> new ArtistaResponseDTO(
                                a.getId(),
                                a.getNome(),
                                a.getNacionalidade(),
                                a.getTipo().toString()
                        ))
                        .collect(Collectors.toSet())
        );
    }

}
