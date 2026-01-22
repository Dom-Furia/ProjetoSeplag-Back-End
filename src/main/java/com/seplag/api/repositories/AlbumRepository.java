package com.seplag.api.repositories;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.artista.TipoArtista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
    Page<Album> findByArtista_Tipo(
            TipoArtista tipo,
            Pageable pageable
    );

    Page<Album> findByArtista_TipoAndArtista_NomeContainingIgnoreCase(
            TipoArtista tipo,
            String nome,
            Pageable pageable
    );

    Page<Album> findByArtista_NomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );
}
