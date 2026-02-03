package com.seplag.api.controller;

import com.seplag.api.domain.album.Album;
import com.seplag.api.dto.AlbumRequestDTO;
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
@Tag(name = "Álbuns V1", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de álbuns (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService, WebSocketNotificationController webSocketNotificationController) {
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
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Album> createAlbumV1(

            @Parameter(description = "Nome do álbum", example = "Hybrid Theory", required = true)
            @RequestParam("nomealbum") String nomealbum,

            @Parameter(description = "Ano de lançamento", example = "2000", required = true)
            @RequestParam("anoLancamento") String anoLancamento,

            @Parameter(
                    description = "Imagem da capa do álbum",
                    content = @Content(schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam(value = "imgUrl", required = false) MultipartFile imgUrl,

            @Parameter(
                    description = "IDs dos artistas vinculados ao álbum",
                    example = "[\"550e8400-e29b-41d4-a716-446655440000\"]",
                    required = true
            )
            @RequestParam("artista_id") Set<UUID> artistaId
    ) {

        AlbumRequestDTO albumRequestDTO =
                new AlbumRequestDTO(nomealbum, anoLancamento, imgUrl, artistaId);

        Album album = albumService.createAlbumV1(albumRequestDTO);

        return ResponseEntity.status(201).body(album);
    }

    //---------------------------------------Listar Albuns-------------------------------//
    @Operation(
            summary = "Listar álbuns",
            description = "Retorna álbuns com paginação, filtro por artista  e ordenação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Album>> getAlbumsV1(

            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int pageSize,

            @Parameter(description = "Filtrar pelo nome do artista", example = "Linkin Park")
            @RequestParam(required = false) String nomeArtista,

            @Parameter(description = "Direção da ordenação (ASC ou DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction order
    ) {

        List<Album> albums = albumService.getAllAlbumsV1(page, pageSize, nomeArtista, order);

        return ResponseEntity.ok(albums);
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
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Album> updatePartialAlbumV1(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id,

            @Parameter(description = "Novo nome do álbum", example = "Meteora")
            @RequestParam(required = false) String nomeAlbum,

            @Parameter(description = "Novo ano de lançamento", example = "2003")
            @RequestParam(required = false) String anoLancamento,

            @Parameter(
                    description = "Nova imagem da capa",
                    content = @Content(schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam(required = false) MultipartFile imgUrl
    ) {

        AlbumUpdateDTO dto =
                new AlbumUpdateDTO(nomeAlbum, anoLancamento, imgUrl);

        Album album = albumService.updatePartialV1(id, dto);

        return ResponseEntity.ok(album);
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
    public ResponseEntity<Album> updateAlbumV1(

            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id,

            @Parameter(description = "Novo nome do álbum", example = "Meteora")
            @RequestParam(required = false) String nomeAlbum,

            @Parameter(description = "Novo ano de lançamento", example = "2003")
            @RequestParam(required = false) String anoLancamento,

            @Parameter(
                    description = "Nova imagem da capa",
                    content = @Content(schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam(required = false) MultipartFile imgUrl
    ) {

        AlbumUpdateDTO dto = new AlbumUpdateDTO(nomeAlbum, anoLancamento, imgUrl);
        Album album = albumService.updateV1(id, dto);

        return ResponseEntity.ok(album);
    }
}
