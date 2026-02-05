package com.seplag.api.service;

import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.ArtistaRequestDTO;
import com.seplag.api.dto.ArtistaResponseDTO;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArtistaService - Testes Unitários")
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository;

    @InjectMocks
    private ArtistaService artistaService;

    // ================= CREATE =================

    @Test
    @DisplayName("Deve criar um artista com sucesso")
    void deveCriarArtista() {

        UUID artistaId = UUID.randomUUID();
        ArtistaRequestDTO dto =
                new ArtistaRequestDTO("Nome Teste", "Brasileiro", "BANDA");

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> {
                    Artista artista = invocation.getArgument(0);
                    artista.setId(artistaId);
                    return artista;
                });

        ArtistaResponseDTO criado = artistaService.createArtistaV1(dto);

        assertThat(criado).isNotNull();
        assertThat(criado.id()).isEqualTo(artistaId);
        assertThat(criado.nome()).isEqualTo("Nome Teste");
        assertThat(criado.nacionalidade()).isEqualTo("Brasileiro");
        assertThat(criado.tipo()).isEqualTo(TipoArtista.BANDA.toString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar artista com campos inválidos")
    void deveLancarExcecaoCampoNulo() {

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO("", "2020", TipoArtista.CANTOR.toString());

        assertThrows(IllegalArgumentException.class,
                () -> artistaService.createArtistaV1(dto));
    }

    // ================= DELETE =================

    @Test
    @DisplayName("Deve excluir artista quando existir")
    void deveExcluirArtistaQuandoExistir() {
        UUID artistaId = UUID.randomUUID();

        when(artistaRepository.existsById(artistaId))
                .thenReturn(true);

        artistaService.deleteByIdV1(artistaId);

        verify(artistaRepository).deleteById(artistaId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir artista inexistente")
    void deveLancarExcecaoQuandoArtistaNaoExistir() {
        UUID artistaId = UUID.randomUUID();

        when(artistaRepository.existsById(artistaId))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> artistaService.deleteByIdV1(artistaId));
    }

    // ================= UPDATE PARCIAL =================

    @Test
    @DisplayName("Deve atualizar apenas os campos informados")
    void deveAtualizarApenasCamposInformados() {
        UUID artistaId = UUID.randomUUID();

        Artista artista = new Artista();
        artista.setId(artistaId);
        artista.setNome("Gustavo Lima");
        artista.setNacionalidade("Antiga");
        artista.setTipo(TipoArtista.CANTOR);

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO(
                        "Jorge e Mateus",
                        "Brasileiro",
                        TipoArtista.CANTOR.toString()
                );

        when(artistaRepository.findById(artistaId))
                .thenReturn(Optional.of(artista));

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponseDTO result =
                artistaService.updatePartialV1(artistaId, dto);

        assertEquals("Jorge e Mateus", result.nome());
        assertEquals("Brasileiro", result.nacionalidade());
        assertEquals(TipoArtista.CANTOR.toString(), result.tipo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar artista inexistente (parcial)")
    void deveLancarExcecaoAoAtualizarArtistaInexistente() {
        UUID artistaId = UUID.randomUUID();

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO("Atualizado", null, null);

        when(artistaRepository.findById(artistaId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> artistaService.updatePartialV1(artistaId, dto));
    }

    // ================= UPDATE COMPLETO =================

    @Test
    @DisplayName("Deve atualizar todos os campos do artista")
    void deveAtualizarTodosOsCampos() {
        UUID artistaId = UUID.randomUUID();

        Artista artista = new Artista();
        artista.setId(artistaId);

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO(
                        "Gustavo Lima",
                        "Brasileiro",
                        TipoArtista.CANTOR.toString()
                );

        when(artistaRepository.findById(artistaId))
                .thenReturn(Optional.of(artista));

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponseDTO result =
                artistaService.updateV1(artistaId, dto);

        assertEquals("Gustavo Lima", result.nome());
        assertEquals("Brasileiro", result.nacionalidade());
        assertEquals(TipoArtista.CANTOR.toString(), result.tipo());
        assertEquals(artistaId, result.id());
    }
}
