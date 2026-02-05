package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.imagem_capa.CapaAlbum;
import com.seplag.api.dto.CapaAlbumResponseDTO;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.CapaAlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CapaAlbumService - Testes Unitários")
class CapaAlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private CapaAlbumRepository capaAlbumRepository;

    @Mock
    private MinioStorageService minioService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CapaAlbumService capaAlbumService;

    private UUID albumId;
    private UUID capaId;
    private Album album;
    private CapaAlbum capa;

    @BeforeEach
    void setup() {
        albumId = UUID.randomUUID();
        capaId = UUID.randomUUID();

        album = new Album();
        album.setId(albumId);

        capa = new CapaAlbum();
        capa.setId(capaId);
        capa.setObjectName("imagem-antiga.jpg");
        capa.setAlbum(album);
        capa.setCriadoEm(Instant.now());
    }

    /* -------------------- CRIAR -------------------- */

    @Test
    @DisplayName("Deve criar capa com sucesso")
    void deveCriarCapaComSucesso() {
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(minioService.upload(multipartFile)).thenReturn("imagem.png");

        CapaAlbumResponseDTO response = capaAlbumService.criar(albumId, multipartFile);

        assertThat(response).isNotNull();
        assertThat(response.objectName()).isEqualTo("imagem.png");

        verify(capaAlbumRepository).save(any(CapaAlbum.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao criar capa com álbum inexistente")
    void deveLancarErroAoCriarCapaComAlbumInexistente() {
        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> capaAlbumService.criar(albumId, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Álbum não encontrado");
    }

    @Test
    @DisplayName("Deve lançar erro ao criar capa com arquivo inválido")
    void deveLancarErroAoCriarCapaComArquivoInvalido() {
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        when(multipartFile.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> capaAlbumService.criar(albumId, multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Arquivo inválido");
    }

    /* -------------------- LISTAR -------------------- */

    @Test
    @DisplayName("Deve listar capas por álbum")
    void deveListarCapasPorAlbum() {
        when(capaAlbumRepository.findByAlbumId(albumId))
                .thenReturn(List.of(capa));

        List<CapaAlbumResponseDTO> capas = capaAlbumService.listarPorAlbum(albumId);

        assertThat(capas).hasSize(1);
        assertThat(capas.get(0).objectName()).isEqualTo("imagem-antiga.jpg");
    }

    /* -------------------- BUSCAR POR ID -------------------- */

    @Test
    @DisplayName("Deve buscar capa por ID")
    void deveBuscarCapaPorId() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.of(capa));

        CapaAlbumResponseDTO response = capaAlbumService.buscarPorId(capaId);

        assertThat(response).isNotNull();
        assertThat(response.objectName()).isEqualTo("imagem-antiga.jpg");
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar capa inexistente")
    void deveLancarErroAoBuscarCapaInexistente() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> capaAlbumService.buscarPorId(capaId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capa não encontrada");
    }

    /* -------------------- DOWNLOAD -------------------- */

    @Test
    @DisplayName("Deve gerar URL para download da capa")
    void deveGerarUrlParaDownload() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.of(capa));
        when(minioService.generateUrl("imagem-antiga.jpg"))
                .thenReturn("http://minio/imagem-antiga.jpg");

        String url = capaAlbumService.download(capaId);

        assertThat(url).isEqualTo("http://minio/imagem-antiga.jpg");
    }

    /* -------------------- UPDATE -------------------- */

    @Test
    @DisplayName("Deve atualizar capa com sucesso")
    void deveAtualizarCapaComSucesso() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.of(capa));
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(minioService.upload(multipartFile)).thenReturn("nova-imagem.jpg");

        CapaAlbumResponseDTO response = capaAlbumService.atualizar(capaId, multipartFile);

        assertThat(response.objectName()).isEqualTo("nova-imagem.jpg");

        verify(minioService).delete("imagem-antiga.jpg");
        verify(minioService).upload(multipartFile);
    }

    /* -------------------- DELETE -------------------- */

    @Test
    @DisplayName("Deve deletar capa com sucesso")
    void deveDeletarCapaComSucesso() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.of(capa));

        capaAlbumService.deletar(capaId);

        verify(minioService).delete("imagem-antiga.jpg");
        verify(capaAlbumRepository).delete(capa);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar capa inexistente")
    void deveLancarErroAoDeletarCapaInexistente() {
        when(capaAlbumRepository.findById(capaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> capaAlbumService.deletar(capaId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Capa não encontrada");
    }
}
