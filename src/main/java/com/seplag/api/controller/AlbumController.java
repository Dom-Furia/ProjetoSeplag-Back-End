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
@RequestMapping("/api/v1/album")
public class AlbumController {

    private final AlbumService albumService;
    public AlbumController(AlbumService albumService) {

        this.albumService = albumService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Album> createAlbumV1(@RequestParam("nomealbum") String nomealbum,
                                             @RequestParam("anoLancamento") String anoLancamento,
                                             @RequestParam(value = "imgUrl", required = false) MultipartFile imgUrl,
                                             @RequestParam("artista_id" ) Set<UUID> artistaId
    ) {
        AlbumRequestDTO albumRequestDTO = new AlbumRequestDTO(
                nomealbum,
                anoLancamento,
                imgUrl,
                artistaId);
        Album album = albumService.createAlbumV1(albumRequestDTO);

        return ResponseEntity.ok().body(album);

    }

    @GetMapping
    public ResponseEntity<List<Album>> getAlbumsV1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String nomeArtista,
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {

        List<Album> albums = albumService.getAllAlbumsV1(page,pageSize,nomeArtista,order);
        return ResponseEntity.ok(albums);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletAlbumV1(@PathVariable UUID id) {
        albumService.deleteByIdV1(id);
        return ResponseEntity.ok(
                Map.of("message","Álbum excluido com sucesso.")
        );
    }

    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Album> updatePartAlbumV1(
            @PathVariable UUID id,
            @RequestParam(required = false) String nomeAlbum,
            @RequestParam(required = false) String anoLancamento,
            @RequestParam(required = false) MultipartFile imgUrl
    ) {
        AlbumUpdateDTO dto = new AlbumUpdateDTO(nomeAlbum, anoLancamento, imgUrl);
        Album album = albumService.updatePartialV1(id, dto);
        return ResponseEntity.ok(album);
    }

}
