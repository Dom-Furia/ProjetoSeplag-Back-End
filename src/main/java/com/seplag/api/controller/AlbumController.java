package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    private final AlbumService albumService;



    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Album> createAlbum(@RequestParam("nomealbum") String nomealbum,
                                             @RequestParam("anoLancamento") String anoLancamento,
                                             @RequestParam(value = "imgUrl", required = false) MultipartFile imgUrl,
                                             @RequestParam("artista_id" ) UUID artistaId
    ) {
        AlbumRequestDTO albumRequestDTO = new AlbumRequestDTO(nomealbum,
                anoLancamento,
                imgUrl,
                artistaId);
        Album album = albumService.createAlbum(albumRequestDTO);
        return ResponseEntity.ok().body(album);

    }
}
