package com.seplag.api.service;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.ArtistaRequestDTO;
import com.seplag.api.repositories.AlbumRepository;
import com.seplag.api.repositories.ArtistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArtistaService {
    private final ArtistaRepository artistaRepository;


    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public Artista createArtista(ArtistaRequestDTO data) {

        Artista newArtista = new Artista();
        newArtista.setNome(data.nome());
        newArtista.setNacionalidade(data.nacionalidade());

        return artistaRepository.save(newArtista);
    }

}
