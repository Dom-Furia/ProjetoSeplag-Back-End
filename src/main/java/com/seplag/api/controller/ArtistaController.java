package com.seplag.api.controller;


import com.seplag.api.dto.ArtistaRequestDTO;
import com.seplag.api.dto.ArtistaResponseDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artista")
@RequiredArgsConstructor
@Tag(
        name = "Artista",
        description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de artistas (Versão 1)"
)
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ArtistaController {

    private final ArtistaService artistaService;

    // ----------------------------- LISTAR ARTISTAS ----------------------------- //
    @Operation(
            summary = "Listar artistas",
            description = "Retorna artistas com paginação, ordenação e filtros por nome, tipo e nacionalidade."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de artistas retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<ArtistaResponseDTO>> listarArtistas(

            @Parameter(description = "Número da página (inicia em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,

            @Parameter(description = "Filtrar pelo nome do artista", example = "Elton John")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Filtrar pelo tipo do artista", example = "BANDA")
            @RequestParam(required = false) String tipo,

            @Parameter(description = "Filtrar pela nacionalidade do artista", example = "Britânico")
            @RequestParam(required = false) String nacionalidade,

            @Parameter(
                    description = "Direção da ordenação",
                    example = "ASC",
                    schema = @Schema(implementation = Sort.Direction.class)
            )
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {

        Page<ArtistaResponseDTO> artistas = artistaService.listarArtistasV1(
                page,
                pageSize,
                nome,
                tipo,
                nacionalidade,
                order
        );

        return ResponseEntity.ok(artistas.getContent());
    }

    // ----------------------------- CRIAR ARTISTA ----------------------------- //
    @Operation(
            summary = "Criar novo artista",
            description = "Cria um artista informando nome, nacionalidade e tipo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArtistaResponseDTO> createArtistaV1(
            @Valid @RequestBody ArtistaRequestDTO dto
    ) {

        return ResponseEntity
                .status(201)
                .body(artistaService.createArtistaV1(dto));
    }

    // ----------------------------- ATUALIZAR ARTISTA (PATCH) ----------------------------- //
    @Operation(
            summary = "Atualizar parcialmente artista",
            description = "Atualiza um ou mais campos do artista.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArtistaResponseDTO> updatePartialArtistaV1(

            @Parameter(
                    description = "ID do artista",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @Valid @RequestBody ArtistaRequestDTO dto
    ) {

        return ResponseEntity.ok(
                artistaService.updatePartialV1(id, dto)
        );
    }

    // ----------------------------- ATUALIZAR ARTISTA (PUT) ----------------------------- //
    @Operation(
            summary = "Atualizar artista",
            description = "Atualiza todos os campos do artista.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArtistaResponseDTO> updateArtistaV1(

            @Parameter(
                    description = "ID do artista",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @Valid @RequestBody ArtistaRequestDTO dto
    ) {

        return ResponseEntity.ok(
                artistaService.updateV1(id, dto)
        );
    }

    // ----------------------------- EXCLUIR ARTISTA ----------------------------- //
    @Operation(
            summary = "Excluir artista",
            description = "Remove um artista pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Artista excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistaV1(

            @Parameter(
                    description = "ID do artista",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id
    ) {

        artistaService.deleteByIdV1(id);
        return ResponseEntity.noContent().build();
    }
}
