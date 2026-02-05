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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artista")
@RequiredArgsConstructor
@Tag(name = "Artista", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de artistas (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ArtistaController {

    private final ArtistaService artistaService;



    //----------------------------- Listar Artistas ----------------------/
    @Operation(
            summary = "Listar artistas",
            description = "Retorna artistas com paginação,ordenação e filtro por tipo e nome de artista"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<ArtistaResponseDTO>> listarArtistas(

            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,

            @Parameter(description = "Filtrar pelo nome do artista", example = "Elton John")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Filtrar pelo tipo do artista", example = "Banda")
            @RequestParam(required = false) String tipo,

            @Parameter(description = "Filtrar pelo nacionalidade do artista", example = "Banda")
            @RequestParam(required = false) String nacionalidade,

            @Parameter(description = "Direção da ordenação (ASC ou DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {
        Page<ArtistaResponseDTO> artitas = artistaService.listarArtistasV1(

                page,
                pageSize,
                nome,
                tipo,
                nacionalidade,
                order
        );
        return ResponseEntity.ok(artitas.getContent());
    }

    //----------------------------- Criar Artista ----------------------/
    @Operation(
            summary = "Criar novo artista",
            description = "Cria um artista informando nome, nacionalidade, tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<ArtistaResponseDTO> createArtistaV1(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
            @Valid @RequestBody ArtistaRequestDTO dto
    ) {

        return ResponseEntity.ok(artistaService.createArtistaV1(dto));
    }

    //----------------- Atualizar Artista PATCH --------------------------------//
    @Operation(
            summary = "Atualizar parcialmente Artista",
            description = "Atualiza um ou mais campos do artista (nome, nacionalidade ou tipo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> updatePartialArtistaV1(
            @Parameter(description = "ID do artista", required = true)
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
            @Valid @RequestBody ArtistaRequestDTO dto
    ) {

        return ResponseEntity.ok(artistaService.updatePartialV1(id, dto));
    }

    //--------------------------- Atualizar Artista PUT ------------------------------//
    @Operation(
            summary = "Atualizar Artista",
            description = "Atualiza os campos do artista (nome, nacionalidade e tipo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> updateArtistaV1(

            @Parameter(description = "ID do artista", required = true)
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistaRequestDTO.class)
                    )
            )
            @Valid @RequestBody ArtistaRequestDTO dto

    ) {

        return ResponseEntity.ok(artistaService.updateV1(id, dto));
    }

    //------------------------------- Excluir Artista ------------------------//
    @Operation(
            summary = "Excluir Artista",
            description = "Remove um artista pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista excluído"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteArtistaV1(
            @Parameter(description = "ID do Artista", required = true)
            @PathVariable UUID id
    ) {

        artistaService.deleteByIdV1(id);

        return ResponseEntity.ok(
                Map.of("message", "Artista excluído com sucesso.")
        );
    }

}
