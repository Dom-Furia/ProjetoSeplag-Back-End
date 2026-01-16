package com.seplag.api.repositories;


import com.seplag.api.domain.artista.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistaRepository extends JpaRepository<Artista, UUID> {
}
