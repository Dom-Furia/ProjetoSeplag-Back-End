package com.seplag.api.repositories;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.artista.TipoArtista;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AlbumRepository extends JpaRepository<Album, UUID> {

    @NonNull Page<Album> findAll(@NonNull Pageable pageable);

    //Filtrar Por Nome de artista
    @Query("SELECT DISTINCT a FROM Album a JOIN a.artistas ar " +
            "WHERE LOWER(ar.nome) LIKE LOWER(CONCAT('%', :nomeArtista, '%'))")
    Page<Album> findByArtistaNome(@Param("nomeArtista") String nomeArtista, Pageable pageable);


    // Filtra por tipo de artista e nome (nome pode ser null)
    @Query("SELECT DISTINCT a FROM Album a JOIN a.artistas ar " +
            "WHERE (:nomeArtista IS NULL OR LOWER(ar.nome) LIKE LOWER(CONCAT('%', :nomeArtista, '%'))) " +
            "AND (:tipoArtista IS NULL OR ar.tipo = :tipoArtista)")
    Page<Album> findByArtistaNomeAndTipo(
            @Param("nomeArtista") String nomeArtista,
            @Param("tipoArtista") TipoArtista tipoArtista,
            Pageable pageable);

}
