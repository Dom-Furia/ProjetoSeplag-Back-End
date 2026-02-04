package com.seplag.api.controller;

import com.seplag.api.dto.CapaAlbumResponseDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.CapaAlbumService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/capa")
@Tag(name = "Capa do Album V1", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão da capa dos albuns (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CapaAlbumController {

    private final CapaAlbumService capaAlbumService;

    public CapaAlbumController(CapaAlbumService capaAlbumService) {
        this.capaAlbumService = capaAlbumService;
    }

    /* CREATE */
    @PostMapping
    public ResponseEntity<CapaAlbumResponseDTO> upload(
            @PathVariable UUID albumId,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.criar(albumId, file)
        );
    }

    /* READ */
    @GetMapping("/listar")
    public ResponseEntity<List<CapaAlbumResponseDTO>> listar(
            @PathVariable UUID albumId
    ) {
        return ResponseEntity.ok(
                capaAlbumService.listarPorAlbum(albumId)
        );
    }

    @GetMapping("/{capaId}")
    public ResponseEntity<CapaAlbumResponseDTO> buscar(
            @PathVariable UUID capaId
    ) {
        return ResponseEntity.ok(
                capaAlbumService.buscarPorId(capaId)
        );
    }

    @GetMapping("/generatelink")
    public ResponseEntity<String> download(@PathVariable UUID capaId) {

        String arquivo = capaAlbumService.download(capaId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arquivo);
    }

    /* UPDATE */
    @PutMapping("/{capaId}")
    public ResponseEntity<CapaAlbumResponseDTO> atualizar(
            @PathVariable UUID capaId,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.atualizar(capaId, file)
        );
    }

    /* DELETE */
    @DeleteMapping("/{capaId}")
    public ResponseEntity<Void> deletar(@PathVariable UUID capaId) {
        capaAlbumService.deletar(capaId);
        return ResponseEntity.noContent().build();
    }
}
