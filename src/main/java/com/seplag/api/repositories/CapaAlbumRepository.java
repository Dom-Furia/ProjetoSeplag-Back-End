package com.seplag.api.repositories;

import com.seplag.api.domain.imagem_capa.CapaAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CapaAlbumRepository extends JpaRepository<CapaAlbum, UUID> {

    List<CapaAlbum> findByAlbumId(UUID albumId);
}
