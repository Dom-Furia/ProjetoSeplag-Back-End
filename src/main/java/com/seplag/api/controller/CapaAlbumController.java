package com.seplag.api.controller;

import com.seplag.api.dto.CapaAlbumResponseDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.CapaAlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(
        name = "Capa do Álbum V1",
        description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão da capa dos álbuns"
)
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CapaAlbumController {

    private final CapaAlbumService capaAlbumService;

    /* CREATE */
    @Operation(
            summary = "Cadastrar capa do álbum",
            description = "Realiza o upload da capa vinculada a um álbum",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Capa cadastrada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
            }
    )
    @PostMapping(value = "/{albumid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CapaAlbumResponseDTO> upload(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID albumid,

            @Parameter(
                    description = "Arquivo da capa",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.criar(albumid, file)
        );
    }

    /* READ */
    @Operation(
            summary = "Listar capas do álbum",
            description = "Retorna todas as capas associadas a um álbum",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
            }
    )
    @GetMapping("/listar/{albumId}")
    public ResponseEntity<List<CapaAlbumResponseDTO>> listar(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID albumId
    ) {
        return ResponseEntity.ok(
                capaAlbumService.listarPorAlbum(albumId)
        );
    }

    @Operation(
            summary = "Buscar capa por ID",
            description = "Retorna os dados de uma capa específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Capa encontrada"),
                    @ApiResponse(responseCode = "404", description = "Capa não encontrada")
            }
    )
    @GetMapping("/{capaId}")
    public ResponseEntity<CapaAlbumResponseDTO> buscar(

            @Parameter(description = "ID da capa", required = true)
            @PathVariable UUID capaId
    ) {
        return ResponseEntity.ok(
                capaAlbumService.buscarPorId(capaId)
        );
    }

    @Operation(
            summary = "Gerar link de download",
            description = "Gera um link para download da capa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link gerado"),
                    @ApiResponse(responseCode = "404", description = "Capa não encontrada")
            }
    )
    @GetMapping("/download/{capaId}")
    public ResponseEntity<String> getDownloadLink(

            @Parameter(description = "ID da capa", required = true)
            @PathVariable UUID capaId
    ) {

        String link = capaAlbumService.download(capaId);
        return ResponseEntity.ok(link);
    }

    /* UPDATE */
    @Operation(
            summary = "Atualizar capa",
            description = "Substitui a capa existente por um novo arquivo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Capa atualizada"),
                    @ApiResponse(responseCode = "404", description = "Capa não encontrada")
            }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CapaAlbumResponseDTO> atualizar(

            @Parameter(description = "ID da capa", required = true)
            @PathVariable UUID id,

            @Parameter(
                    description = "Novo arquivo da capa",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                capaAlbumService.atualizar(id, file)
        );
    }

    /* DELETE */
    @Operation(
            summary = "Excluir capa",
            description = "Remove uma capa do sistema",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Capa removida"),
                    @ApiResponse(responseCode = "404", description = "Capa não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(

            @Parameter(description = "ID da capa", required = true)
            @PathVariable UUID id
    ) {
        capaAlbumService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
