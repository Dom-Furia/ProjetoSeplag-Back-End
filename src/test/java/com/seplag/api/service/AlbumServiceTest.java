package com.seplag.api.service;

import com.seplag.api.controller.WebSocketNotificationController;
import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumResponseDTO;
import com.seplag.api.dto.AlbumUpdateDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;


    @Mock
    private WebSocketNotificationController webSocketNotificationController;


    @Test
    @DisplayName("Deve Criar Album Com Sucesso E Enviar WebSocket")
    public void deveCriarAlbumComSucessoEEnviarWebSocket() {

        UUID artistaId = UUID.randomUUID();

        AlbumRequestDTO dto = new AlbumRequestDTO(
                "Hybrid Theory",
                "2000",
                Set.of(artistaId)
        );

        Artista artista = new Artista();
        artista.setId(artistaId);
        artista.setTipo(TipoArtista.CANTOR);
        artista.setNome("Linkin Park");

        when(artistaRepository.findAllById(Set.of(artistaId)))
                .thenReturn(List.of(artista));

        when(albumRepository.save(any(Album.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlbumResponseDTO response = albumService.createAlbumV1(dto);

        assertNotNull(response);
        assertEquals("Hybrid Theory", response.nomeAlbum());
        assertEquals("2000", response.anoLancamento());

        verify(albumRepository, times(1)).save(any(Album.class));
        verify(webSocketNotificationController, times(1))
                .notifyNewAlbum(any(Album.class));

    }

    @Test
    public void deveRetornarAlbumComOuSemFiltro() {
        UUID artistaId = UUID.randomUUID();

        Artista artista = new Artista();
        artista.setId(artistaId);
        artista.setNome("Linkin Park");
        artista.setTipo(TipoArtista.BANDA);

        Album album = new Album();
        album.setId(UUID.randomUUID());
        album.setNomeAlbum("Hybrid Theory");
        album.setAnoLancamento("2000");
        album.setArtistas(Set.of(artista));

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "nomeAlbum")
        );

        Page<Album> page = new PageImpl<>(List.of(album), pageable, 1);

        when(albumRepository.findByArtistaNomeAndTipo(
                eq("Linkin Park"),
                eq(TipoArtista.BANDA),
                any(Pageable.class)
        )).thenReturn(page);

        // when
        List<AlbumResponseDTO> result =
                albumService.getAllAlbunsV1(
                        0,
                        10,
                        "Linkin Park",
                        "BANDA",
                        Sort.Direction.ASC
                );

        // then
        assertEquals(1, result.size());
        assertEquals("Hybrid Theory", result.get(0).nomeAlbum());

        verify(albumRepository, times(1))
                .findByArtistaNomeAndTipo(
                        eq("Linkin Park"),
                        eq(TipoArtista.BANDA),
                        any(Pageable.class)
                );
    }


    @Test
    public void deveAtualizarTodosOsCampos() {
        UUID albumId = UUID.randomUUID();

        Album album = new Album();
        album.setId(albumId);

        AlbumRequestDTO dto =
                new AlbumRequestDTO("Gustavo Lima", "2024", null);

        when(albumRepository.findById(albumId))
                .thenReturn(Optional.of(album));

        when(albumRepository.save(any(Album.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlbumResponseDTO result = albumService.updateV1(albumId, dto);

        assertEquals("Novo Nome", result.nomeAlbum());
        assertEquals("2024", result.anoLancamento());
    }

    @Test
   public void deveAtualizarApenasCamposInformados() {
        UUID albumId = UUID.randomUUID();

        Album album = new Album();
        album.setId(albumId);
        album.setNomeAlbum("Original");
        album.setAnoLancamento("2020");

        AlbumRequestDTO dto =
                new AlbumRequestDTO("Atualizado", null, null);

        when(albumRepository.findById(albumId))
                .thenReturn(Optional.of(album));

        when(albumRepository.save(any(Album.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlbumResponseDTO result = albumService.updatePartialV1(albumId, dto);

        assertEquals("Atualizado", result.nomeAlbum());
        assertEquals("2020", result.anoLancamento());
    }


    @Test
    public void deveExcluirAlbumQuandoExistir() {
        UUID albumId = UUID.randomUUID();

        when(albumRepository.existsById(albumId)).thenReturn(true);

        albumService.deleteByIdV1(albumId);

        verify(albumRepository).deleteById(albumId);
    }

    //-------------------------------Teste De Exeções-------------------------------//
    @Test
    @DisplayName("Quando tentar deletar um album pelo ID")
    public void deveLancarExcecaoQuandoAlbumNaoExistir() {
        UUID albumId = UUID.randomUUID();

        when(albumRepository.existsById(albumId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> albumService.deleteByIdV1(albumId));
    }

    @Test
    @DisplayName("Quando tentar Atualizar um album que não existe")
   public void deveLancarExcecaoQuandoTentarAtualizarAlbumInesistente() {
        UUID albumId = UUID.randomUUID();

        AlbumRequestDTO dto =
                new AlbumRequestDTO("Atualizado", null, null);


        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.updatePartialV1(albumId,dto));
    }

    @Test
    @DisplayName("Deve lança Exeção quando não preencher todos os campos")
   public void deveLancarExcecaoCampoNulo() {
        UUID albumId = UUID.randomUUID();

        AlbumRequestDTO dto = new AlbumRequestDTO(
                "",
                "2020",
                Set.of(albumId)

        );
        assertThrows(IllegalArgumentException.class,() -> albumService.createAlbumV1(dto));
    }

    @Test
    void deveListarAlbunsQuandoTipoForNulo() {
        Album album = new Album();
        album.setId(UUID.randomUUID());
        album.setNomeAlbum("Album Teste");

        Page<Album> page = new PageImpl<>(List.of(album));

        when(albumRepository.findByArtistaNomeAndTipo(
                anyString(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        List<AlbumResponseDTO> result =
                albumService.getAllAlbunsV1(
                        0,
                        10,
                        "Qualquer",
                        null,
                        Sort.Direction.ASC
                );

        assertEquals(1, result.size());
    }

    @Test
    void deveUsarNomeVazioQuandoNomeArtistaForBlank() {
        Page<Album> page = new PageImpl<>(List.of());

        when(albumRepository.findByArtistaNomeAndTipo(
                eq(""),
                any(),
                any(Pageable.class)
        )).thenReturn(page);

        albumService.getAllAlbunsV1(
                0,
                10,
                " ",
                "banda",
                Sort.Direction.ASC
        );

        verify(albumRepository).findByArtistaNomeAndTipo(
                eq(""),
                eq(TipoArtista.BANDA),
                any(Pageable.class)
        );
    }



}