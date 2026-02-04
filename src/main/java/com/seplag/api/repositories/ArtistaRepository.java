package com.seplag.api.repositories;


import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.TipoArtista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, UUID> {

    @Query("""
        SELECT a FROM Artista a
        WHERE (:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:tipo IS NULL OR a.tipo = :tipo)
          AND (:nacionalidade IS NULL OR LOWER(a.nacionalidade) LIKE LOWER(CONCAT('%', :nacionalidade, '%')))
    """)
    Page<Artista> filtrar(
            @Param("nome") String nome,
            @Param("tipo") TipoArtista tipo,
            @Param("nacionalidade") String nacionalidade,
            Pageable pageable
    );
}
