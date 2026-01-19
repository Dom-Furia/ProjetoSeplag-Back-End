package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.album.AlbumResponseDTO;
import com.seplag.api.service.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> getAlbums(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        List<AlbumResponseDTO> allAlbums = albumService.getAllAlbums(page, pageSize);
        return ResponseEntity.ok(allAlbums);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleAlbum(@PathVariable UUID id) {
        albumService.deleteById(id);
        return ResponseEntity.ok(
                Map.of("message","Álbum excluido com sucesso.")
        );
    }

}
