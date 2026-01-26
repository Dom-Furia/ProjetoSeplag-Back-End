package com.seplag.api.controller;


import com.seplag.api.domain.artista.Artista;
import com.seplag.api.domain.artista.ArtistaRequestDTO;
import com.seplag.api.service.AlbumService;
import com.seplag.api.service.ArtistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artista")
public class ArtistaController {

    private final ArtistaService artistaService;
    private AlbumService albumService;

    public ArtistaController(AlbumService albumService, ArtistaService artistaService) {
        this.albumService = albumService;
        this.artistaService = artistaService;
    }

    @PostMapping
    public ResponseEntity<Artista> createArtistaV1(@RequestBody ArtistaRequestDTO artistaRequestDTO) {
        Artista artista = artistaService.createArtistaV1(artistaRequestDTO);
        return ResponseEntity.ok(artista);
    }

}
