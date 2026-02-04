package com.seplag.api.service;


import com.seplag.api.domain.artista.Artista;
import com.seplag.api.dto.ArtistaRequestDTO;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.dto.ArtistaResponseDTO;
import com.seplag.api.repositories.ArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArtistaService {
    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    //---------------------------- Criar Artista ------------------------//
    @Transactional
    public ArtistaResponseDTO createArtistaV1(ArtistaRequestDTO dto) {

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("O campo nome é obrigatório");
        }

        if (dto.tipo() == null || dto.tipo().isBlank()) {
            throw new IllegalArgumentException("O campo tipo é obrigatório");
        }

        Artista newArtista = new Artista();
        newArtista.setNome(dto.nome());
        newArtista.setNacionalidade(dto.nacionalidade());
        newArtista.setTipo(TipoArtista.valueOf(dto.tipo().toUpperCase()));

        artistaRepository.save(newArtista);

        return new ArtistaResponseDTO(
                newArtista.getId(),
                newArtista.getNome(),
                newArtista.getNacionalidade(),
                newArtista.getTipo().toString());
    }

    //---------------------------- Listar Artistas ------------------------//
    @Transactional(readOnly = true)
    public Page<ArtistaResponseDTO> listarArtistasV1(
            int page,
            int pageSize,
            String nome,
            String tipo,
            String nacionalidade,
            Sort.Direction order
    ) {

        TipoArtista tipoEnum = null;
        if (tipo != null && !tipo.isBlank()) {
            try {
                tipoEnum = TipoArtista.valueOf(tipo.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tipo de artista inválido");
            }
        }

        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by(order, "nome")
        );

        Page<Artista> result = artistaRepository.filtrar(
                nome,
                tipoEnum,
                nacionalidade,
                pageable
        );

        return result.map(this::toResponseDTO);
    }


    //--------------------------------------- Excluir Artista-----------------------//
    @Transactional
    public void  deleteByIdV1(UUID id) {
        if (!artistaRepository.existsById(id)) {
            throw new EntityNotFoundException("Artista não encontrado");
        }
        artistaRepository.deleteById(id);
    }

    //---------------------------- Atualizar Album Parcial ------------------------//
    @Transactional
    public ArtistaResponseDTO updatePartialV1(UUID id, ArtistaRequestDTO dto) {

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

        Artista newArtista = artistaRepository.save(artista);

        return new ArtistaResponseDTO(
                newArtista.getId(),
                newArtista.getNome(),
                newArtista.getNacionalidade(),
                newArtista.getTipo().toString()
        );
    }


    //---------------------------- Atualizar Artista ------------------------//
    @Transactional
    public ArtistaResponseDTO updateV1(UUID id, ArtistaRequestDTO dto) {

        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        artista.setNome(dto.nome());
        artista.setNacionalidade(dto.nacionalidade());
        artista.setTipo(TipoArtista.valueOf(dto.tipo().toUpperCase()));
        Artista newArtista = artistaRepository.save(artista);

        return new ArtistaResponseDTO(
                newArtista.getId(),
                newArtista.getNome(),
                newArtista.getNacionalidade(),
                newArtista.getTipo().toString()
        );
    }

    // Converter um Artista em ArtistaResponseDTO
    private ArtistaResponseDTO toResponseDTO(Artista artista) {
        return new ArtistaResponseDTO(
                artista.getId(),
                artista.getNome(),
                artista.getNacionalidade(),
                artista.getTipo().toString()
        );
    }


}
