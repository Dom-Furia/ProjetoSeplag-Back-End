package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.domain.album.AlbumResponseDTO;
import com.seplag.api.domain.album.AlbumUpdateDTO;
import com.seplag.api.domain.artista.TipoArtista;
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
    public ResponseEntity<List<AlbumResponseDTO>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false)TipoArtista tipo,
            @RequestParam(required = false)String nomeArtista

            ) {
        List<AlbumResponseDTO> allAlbums = albumService.getAllAlbums(page, pageSize, null, null);
        return ResponseEntity.ok(allAlbums);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleAlbum(@PathVariable UUID id) {
        albumService.deleteById(id);
        return ResponseEntity.ok(
                Map.of("message","Álbum excluido com sucesso.")
        );
    }

    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Album> updateAlbum(
            @PathVariable UUID id,
            @RequestParam(required = false) String nomeAlbum,
            @RequestParam(required = false) String anoLancamento,
            @RequestParam(required = false) MultipartFile imgUrl
    ) {
        AlbumUpdateDTO dto = new AlbumUpdateDTO(nomeAlbum, anoLancamento, imgUrl);
        Album album = albumService.updatePartial(id, dto);
        return ResponseEntity.ok(album);
    }

}
