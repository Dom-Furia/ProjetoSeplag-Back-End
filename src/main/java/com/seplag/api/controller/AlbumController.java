package com.seplag.api.controller;


import com.seplag.api.dto.AlbumRequestDTO;
import com.seplag.api.dto.AlbumResponseDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Álbuns V1", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de álbuns (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    //-------------------------------Criar Album----------------------------------//
    @Operation(
            summary = "Criar novo álbum",
            description = "Cria um álbum informando nome, ano de lançamento, artistas vinculados e imagem."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<AlbumResponseDTO> createAlbumV1(
            @Valid @RequestBody AlbumRequestDTO albumRequestDTO
    ) {

        return ResponseEntity.status(201).body(albumService.createAlbumV1(albumRequestDTO));
    }

    //---------------------------------------Listar Albuns-------------------------------//
    @Operation(
            summary = "Listar álbuns",
            description = "Retorna álbuns com paginação,ordenação e filtro por tipo e nome de artista "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> getAlbumsV1(

            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,

            @Parameter(description = "Filtrar pelo nome do artista", example = "Linkin Park")
            @RequestParam(required = false) String nomeArtista,

            @Parameter(description = "Filtrar pelo tipo do artista", example = "Banda")
            @RequestParam(required = false) String tipo,

            @Parameter(description = "Direção da ordenação (ASC ou DESC)", example = "ASC")
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
            @ApiResponse(responseCode = "200", description = "Álbum excluído"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
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

    //----------------------------------------Atualizar Album PATCH-----------------------------------//
    @Operation(
            summary = "Atualizar parcialmente álbum",
            description = "Atualiza um ou mais campos do álbum (nome, ano ou imagem)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> updatePartialAlbumV1(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id,

            @RequestBody AlbumRequestDTO dto

    ) {


        return ResponseEntity.ok(albumService.updatePartialV1(id, dto));
    }

    //----------------------------------------Atualizar Album PUT-----------------------------------//
    @Operation(
            summary = "Atualizar álbum",
            description = "Atualiza os campos do álbum (nome, ano ou imagem)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<AlbumResponseDTO> updateAlbumV1(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id,

            @RequestBody AlbumRequestDTO dto

    ) {

        return ResponseEntity.ok(albumService.updateV1(id, dto));
    }
}
