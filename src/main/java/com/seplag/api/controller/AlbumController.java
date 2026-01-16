package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.domain.album.AlbumRequestDTO;
import com.seplag.api.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody AlbumRequestDTO body) {
        Album newAlbum = this.albumService.createAlbum(body);
        return ResponseEntity.ok().body(newAlbum);

    }
}
