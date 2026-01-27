package com.seplag.api.service;


import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.ArtistaRequestDTO;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArtistaService {
    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {

        this.artistaRepository = artistaRepository;
    }

    @Transactional
    public Artista createArtistaV1(ArtistaRequestDTO data) {

        Artista newArtista = new Artista();
        newArtista.setNome(data.nome());
        newArtista.setNacionalidade(data.nacionalidade());
        newArtista.setTipo(TipoArtista.valueOf(data.tipo().toUpperCase()));

        return artistaRepository.save(newArtista);
    }

    @Transactional
    public void  deleteByIdV1(UUID id) {
        if (!artistaRepository.existsById(id)) {
            throw new EntityNotFoundException("Artista não encontrado");
        }
        artistaRepository.deleteById(id);
    }

    @Transactional
    public Artista updatePartialV1(UUID id, ArtistaRequestDTO dto) {

        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        if (dto.nome() != null) {
            artista.setNome(dto.nome());
        }

        if (dto.nacionalidade() != null) {
            artista.setNacionalidade(dto.nacionalidade());
        }

        if (dto.tipo() != null) {
            artista.setTipo(TipoArtista.valueOf(dto.tipo().toUpperCase()));
        }

        return artistaRepository.save(artista);
    }

    @Transactional
    public Artista updateV1(UUID id, ArtistaRequestDTO dto) {

        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        artista.setNome(dto.nome());
        artista.setNacionalidade(dto.nacionalidade());
        artista.setTipo(TipoArtista.valueOf(dto.tipo().toUpperCase()));

        return artistaRepository.save(artista);
    }

}
