package com.seplag.api.controller;

import com.seplag.api.dto.CapaAlbumResponseDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.CapaAlbumService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/capa")
@RequiredArgsConstructor
@Tag(name = "Capa do Album V1", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão da capa dos albuns (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CapaAlbumController {

    private final CapaAlbumService capaAlbumService;


    /* CREATE */
    @PostMapping(value = "/{albumid}", consumes = "multipart/form-data")
    public ResponseEntity<CapaAlbumResponseDTO> upload(
            @PathVariable UUID albumid,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.criar(albumid, file)
        );
    }

    /* READ */
    @GetMapping("/listar/{albumId}")
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

    @GetMapping("/download/{capaId}")
    public ResponseEntity<String> getDownloadLink(@PathVariable UUID capaId) {

        String link = capaAlbumService.download(capaId);

        return ResponseEntity.ok(link);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<CapaAlbumResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.atualizar(id, file)
        );
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        capaAlbumService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
