package com.seplag.api.service;


import com.seplag.api.domain.artista.Artista;
import com.seplag.api.dto.ArtistaRequestDTO;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.ArtistaResponseDTO;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository;

    @InjectMocks
    private ArtistaService artistaService;

    @Test
    @DisplayName("Deve criar um artista corretamente")
    void deveCriarArtista() {

        UUID artistaId = UUID.randomUUID();
        ArtistaRequestDTO dto = new ArtistaRequestDTO("Nome Teste", "Brasileiro", "BANDA");

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> {
                    Artista artista = invocation.getArgument(0);
                    artista.setId(artistaId);
                    return artista;
                });

        // Executa o método
        ArtistaResponseDTO criado = artistaService.createArtistaV1(dto);

        // Verificações
        assertThat(criado).isNotNull();
        assertThat(criado.id()).isEqualTo(artistaId);
        assertThat(criado.nome()).isEqualTo("Nome Teste");
        assertThat(criado.nacionalidade()).isEqualTo("Brasileiro");
        assertThat(criado.tipo()).isEqualTo(TipoArtista.BANDA.toString());
    }

    @Test
    @DisplayName("Deve verificar se existe e excluir corretamente")
    public void deveExcluirArtistaQuandoExistir() {
        UUID albumId = UUID.randomUUID();

        when(artistaRepository.existsById(albumId)).thenReturn(true);

        artistaService.deleteByIdV1(albumId);

        verify(artistaRepository).deleteById(albumId);
    }

    @Test
    @DisplayName("Deve atualizar somente os campos informados")
    public void deveAtualizarApenasCamposInformados() {
        UUID artistaId = UUID.randomUUID();

        Artista artista = new Artista();
        artista.setId(artistaId);
        artista.setNome("Gustavo Lima");
        artista.setNacionalidade("2020");
        artista.setTipo(TipoArtista.CANTOR);

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO("Jorge e Matheus", "Brasileiro", TipoArtista.CANTOR.toString());

        when(artistaRepository.findById(artistaId))
                .thenReturn(Optional.of(artista));

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponseDTO result = artistaService.updatePartialV1(artistaId, dto);

        assertEquals("Jorge e Matheus", result.nome());
        assertEquals("Brasileiro", result.nacionalidade());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos informados")
    public void deveAtualizarTodosOsCampos() {
        UUID artistaId = UUID.randomUUID();

        Artista artista = new Artista();
        artista.setId(artistaId);

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO("Gustavo Lima",
                        "Brasileiro",
                        TipoArtista.CANTOR.toString());

        when(artistaRepository.findById(artistaId))
                .thenReturn(Optional.of(artista));

        when(artistaRepository.save(any(Artista.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistaResponseDTO result = artistaService.updateV1(artistaId, dto);

        assertEquals("Gustavo Lima", result.nome());
        assertEquals(artistaId, result.id());
        assertEquals("Brasileiro", result.nacionalidade());
    }

    //-------------------------------Teste De Exceções-------------------------------//

    @Test
    @DisplayName("Quando tentar deletar um artista pelo ID")
    public void deveLancarExcecaoQuandoArtistaNaoExistir() {
        UUID artistaId = UUID.randomUUID();

        when(artistaRepository.existsById(artistaId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> artistaService.deleteByIdV1(artistaId));
    }

    @Test
    @DisplayName("Quando tentar Atualizar um artista que não existe")
    public void deveLancarExcecaoQuandoTentarAtualizarArtistaInexistente() {
        UUID albumId = UUID.randomUUID();

        ArtistaRequestDTO dto =
                new ArtistaRequestDTO(
                        "Atualizado",
                        null,
                        null);


        when(artistaRepository.findById(albumId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> artistaService.updatePartialV1(albumId,dto));
    }

    @Test
    @DisplayName("Deve lança Exeção quando não preencher todos os campos")
    public void deveLancarExcecaoCampoNulo() {

        ArtistaRequestDTO dto = new ArtistaRequestDTO(
                "",
                "2020",
                TipoArtista.CANTOR.toString()

        );
        assertThrows(IllegalArgumentException.class,
                () -> artistaService.createArtistaV1(dto));
    }


}