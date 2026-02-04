package com.seplag.api.repositories;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Test
    @DisplayName("Deve salvar álbum com artista e recuperar por nome do artista")
    void testFindByArtistaNome() {
        // Arrange: criar artista e álbum
        Artista artista = new Artista();
        artista.setNome("Serj Tankian");
        artista.setNacionalidade("Armênia");
        artista.setTipo(TipoArtista.CANTOR);
        artistaRepository.save(artista);

        Album album = new Album();
        album.setNomeAlbum("Harakiri");
        album.setAnoLancamento("2000");
        album.setArtistas(Set.of(artista));
        albumRepository.save(album);

        // Act: buscar pelo nome do artista
        Page<Album> result = albumRepository.findByArtistaNome("serj", PageRequest.of(0, 10));

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().getFirst().getNomeAlbum()).isEqualTo("Harakiri");
    }

    @Test
    @DisplayName("Deve filtrar álbum por tipo e nome do artista")
    void testFindByArtistaNomeAndTipo() {
        Artista artista = new Artista();
        artista.setNacionalidade("Armênia");
        artista.setNome("Mike Shinoda");
        artista.setTipo(TipoArtista.CANTOR);
        artistaRepository.save(artista);

        Album album = new Album();
        album.setNomeAlbum("The Rising Tied");
        album.setAnoLancamento("2000");
        album.setArtistas(Set.of(artista));
        albumRepository.save(album);

        // Act: filtrar por nome e tipo
        Page<Album> result = albumRepository.findByArtistaNomeAndTipo(
                "mike", TipoArtista.CANTOR, PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().getFirst().getNomeAlbum()).isEqualTo("The Rising Tied");
    }
}