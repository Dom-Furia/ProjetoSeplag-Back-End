package com.seplag.api.service;

import com.seplag.api.controller.WebSocketNotificationController;
import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumUpdateDTO;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlbumService - Testes Unitários")
class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private WebSocketNotificationController webSocketNotificationController;

    private Artista artista;
    private Album album;

    @BeforeEach
    void setup() {
        artista = new Artista();
        artista.setId(UUID.randomUUID());
        artista.setNome("Artista Teste");
        artista.setNacionalidade("Brasil");
        artista.setTipo(TipoArtista.CANTOR);

        album = new Album();
        album.setId(UUID.randomUUID());
        album.setNomeAlbum("Album Teste");
        album.setAnoLancamento("2024");
        Set<Artista> artistas = new HashSet<>();
        artistas.add(artista);
        album.setArtistas(artistas);;
    }

    // ================= CREATE =================

    @Test
    @DisplayName("Deve criar álbum com sucesso")
    void createAlbumV1_sucesso() {
        AlbumRequestDTO dto = new AlbumRequestDTO(
                "Novo Album",
                "2025",
                Set.of(artista.getId())
        );

        when(artistaRepository.findAllById(dto.artistaIds()))
                .thenReturn(List.of(artista));

        when(albumRepository.save(any(Album.class)))
                .thenReturn(album);

        var response = albumService.createAlbumV1(dto);

        assertNotNull(response);
        assertEquals("Album Teste", response.nomeAlbum());
        verify(webSocketNotificationController)
                .notifyNewAlbum(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome do álbum estiver vazio")
    void createAlbumV1_semNome_deveLancarExcecao() {
        AlbumRequestDTO dto = new AlbumRequestDTO(
                "",
                "2024",
                Set.of(UUID.randomUUID())
        );

        assertThrows(IllegalArgumentException.class,
                () -> albumService.createAlbumV1(dto));
    }

    @Test
    @DisplayName("Deve lançar exceção quando artista não for encontrado")
    void createAlbumV1_artistaNaoEncontrado() {
        AlbumRequestDTO dto = new AlbumRequestDTO(
                "Album",
                "2024",
                Set.of(UUID.randomUUID())
        );

        when(artistaRepository.findAllById(any()))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class,
                () -> albumService.createAlbumV1(dto));
    }

    // ================= ADICIONAR ARTISTAS =================

    @Test
    @DisplayName("Deve adicionar artistas ao álbum com sucesso")
    void adicionarArtistas_sucesso() {
        when(albumRepository.findById(album.getId()))
                .thenReturn(Optional.of(album));

        when(artistaRepository.findAllById(any()))
                .thenReturn(List.of(artista));

        var response = albumService.adicionarArtistas(
                album.getId(),
                Set.of(artista.getId())
        );

        assertFalse(response.artistas().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar artistas em álbum inexistente")
    void adicionarArtistas_albumNaoEncontrado() {
        when(albumRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.adicionarArtistas(
                        UUID.randomUUID(),
                        Set.of(UUID.randomUUID())
                ));
    }

    // ================= LISTAR =================

    @Test
    @DisplayName("Deve listar álbuns com paginação e filtros")
    void getAllAlbunsV1_sucesso() {
        Page<Album> page = new PageImpl<>(List.of(album));

        when(albumRepository.findByArtistaNomeAndTipo(
                anyString(),
                any(),
                any(Pageable.class)))
                .thenReturn(page);

        var result = albumService.getAllAlbunsV1(
                0, 10, "", null, Sort.Direction.ASC
        );

        assertEquals(1, result.size());
    }

    // ================= DELETE =================

    @Test
    @DisplayName("Deve excluir álbum com sucesso")
    void deleteByIdV1_sucesso() {
        UUID id = UUID.randomUUID();

        when(albumRepository.existsById(id))
                .thenReturn(true);

        albumService.deleteByIdV1(id);

        verify(albumRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir álbum inexistente")
    void deleteByIdV1_naoEncontrado() {
        UUID id = UUID.randomUUID();

        when(albumRepository.existsById(id))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> albumService.deleteByIdV1(id));
    }

    // ================= UPDATE =================

    @Test
    @DisplayName("Deve atualizar parcialmente o álbum")
    void updatePartialV1_sucesso() {
        AlbumUpdateDTO dto = new AlbumUpdateDTO(
                "Novo Nome",
                null
        );

        when(albumRepository.findById(album.getId()))
                .thenReturn(Optional.of(album));

        when(albumRepository.save(any()))
                .thenReturn(album);

        var response = albumService.updatePartialV1(album.getId(), dto);

        assertEquals("Novo Nome", response.nomeAlbum());
    }

    @Test
    @DisplayName("Deve atualizar álbum completamente")
    void updateV1_sucesso() {
        AlbumUpdateDTO dto = new AlbumUpdateDTO(
                "Nome Atualizado",
                "2026"
        );

        when(albumRepository.findById(album.getId()))
                .thenReturn(Optional.of(album));

        when(albumRepository.save(any()))
                .thenReturn(album);

        var response = albumService.updateV1(album.getId(), dto);

        assertEquals("Nome Atualizado", response.nomeAlbum());
        assertEquals("2026", response.anoLancamento());
    }
}
