package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.album.AlbumUpdateDTO;
import com.seplag.api.domain.artista.TipoArtista;
import com.seplag.api.service.AlbumService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/album")
public class AlbumControllerV2 {

    private final AlbumService albumService;
    public AlbumControllerV2(AlbumService albumService) {

        this.albumService = albumService;
    }


    @GetMapping
    public ResponseEntity<List<Album>> getAlbumsV2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String nomeArtista,
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {

        List<Album> albums = albumService.getAllAlbumsV2(page, pageSize, nomeArtista, tipo, order);
        return ResponseEntity.ok(albums);
    }



}
