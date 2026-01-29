package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v2/album")
@Tag(name = "Álbuns V2", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de álbuns (Versão 2)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AlbumControllerV2 {

    private final AlbumService albumService;
    public AlbumControllerV2(AlbumService albumService) {

        this.albumService = albumService;
    }

    @Operation(
            summary = "Listar álbuns",
            description = "Retorna álbuns com paginação,ordenação, filtro por artista e filtro por tipo de artista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
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
