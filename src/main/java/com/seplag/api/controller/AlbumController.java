package com.seplag.api.controller;


import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumResponseDTO;
import com.seplag.api.dto.AlbumUpdateDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.AlbumService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/v1/album")
@RequiredArgsConstructor
@Tag(name = "Álbuns V1", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de álbuns (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AlbumController {

    private final AlbumService albumService;


    //-------------------------------Criar Album----------------------------------//
    @Operation(
            summary = "Criar novo álbum",
            description = "Cria um álbum informando nome, ano de lançamento e artistas vinculados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlbumRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponseDTO> createAlbumV1(
            @Valid @RequestBody AlbumRequestDTO album
    ) {

        return ResponseEntity.status(201).body(albumService.createAlbumV1(album));
    }

    //---------------------------------------Listar Albuns-------------------------------//
    @Operation(
            summary = "Listar álbuns",
            description = "Retorna álbuns com paginação, ordenação e filtros por nome e tipo do artista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de álbuns retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de consulta inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> getAlbumsV1(

            @Parameter(
                    description = "Número da página (inicia em 0)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Quantidade de registros por página",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int pageSize,

            @Parameter(
                    description = "Filtrar pelo nome do artista",
                    example = "Linkin Park"
            )
            @RequestParam(required = false) String nomeArtista,

            @Parameter(
                    description = "Filtrar pelo tipo do artista",
                    example = "BANDA"
            )
            @RequestParam(required = false) String tipo,

            @Parameter(
                    description = "Direção da ordenação",
                    example = "ASC",
                    schema = @Schema(implementation = Sort.Direction.class)
            )
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {

      List<AlbumResponseDTO> albuns =  albumService.getAllAlbunsV1(
                page,
                pageSize,
                nomeArtista,
                tipo,
                order
        );

        return ResponseEntity.ok(albuns);
    }

    //--------------------------------------Excluir Album------------------------------------//
    @Operation(
            summary = "Excluir álbum",
            description = "Remove um álbum pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAlbumV1(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id
    ) {

        albumService.deleteByIdV1(id);

        return ResponseEntity.ok(
                Map.of("message", "Álbum excluído com sucesso.")
        );
    }

    //----------------------------------------Atualizar Album Parcial -----------------------------------//
    @Operation(
            summary = "Atualizar parcialmente álbum",
            description = "Atualiza um ou mais campos do álbum.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlbumRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponseDTO> updatePartialAlbumV1(

            @Parameter(
                    description = "ID do álbum",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

           @Valid @RequestBody AlbumUpdateDTO album

    ) {

        return ResponseEntity.ok(albumService.updatePartialV1(id, album));
    }

    //----------------------------------------Atualizar Album -----------------------------------//
    @Operation(
            summary = "Atualizar  álbum",
            description = "Atualiza todos os campos do álbum.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AlbumUpdateDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponseDTO> updateAlbumV1(

            @Parameter(
                    description = "ID do álbum",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @Valid @RequestBody AlbumUpdateDTO album

    ) {

        return ResponseEntity.ok(albumService.updateV1(id, album));
    }

    //----------------------------- Adicionar Artistas ao Album ----------------------//
    @Operation(
            summary = "Adicionar artistas ao álbum",
            description = "Vincula um ou mais artistas a um álbum existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    description = "Lista de IDs dos artistas",
                                    example = "[\"550e8400-e29b-41d4-a716-446655440000\"]",
                                    type = "array",
                                    implementation = UUID.class
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artistas adicionados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum ou artista não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/adicionar/artistas/{albumId}")
    public ResponseEntity<AlbumResponseDTO> adicionarArtistas(

            @Parameter(
                    description = "ID do álbum",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID albumId,

            @Valid @RequestBody Set<UUID> artistasIds
    ) {

        return ResponseEntity.ok(albumService.adicionarArtistas(albumId, artistasIds));
    }


}
