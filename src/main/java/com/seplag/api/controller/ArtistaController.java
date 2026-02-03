package com.seplag.api.controller;



import com.seplag.api.domain.artista.Artista;
import com.seplag.api.dto.ArtistaRequestDTO;
import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artista")
@Tag(name = "Artista", description = "Endpoints responsáveis pelo cadastro, consulta, atualização e exclusão de artistas (Versão 1)")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ArtistaController {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
    }

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
    public ResponseEntity<Artista> createArtistaV1(@RequestBody ArtistaRequestDTO artistaRequestDTO) {
        Artista artista = artistaService.createArtistaV1(artistaRequestDTO);
        return ResponseEntity.ok(artista);
    }


    @Operation(
            summary = "Atualizar parcialmente Artista",
            description = "Atualiza um ou mais campos do artista (nome, nacionalidade ou tipo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado")
    })
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Artista> updatePartialArtistaV1(

            @Parameter(description = "ID do artista", required = true)
            @PathVariable UUID id,

            @Parameter(description = "Novo nome do artista", example = "Elton John")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Novo nacionalidade", example = "Americano")
            @RequestParam(required = false) String nacionalidade,

            @Parameter(description = "Novo tipo", example = "CANTOR")
            @RequestParam(required = false) String tipo
    ) {

        ArtistaRequestDTO dto = new ArtistaRequestDTO(nome, nacionalidade, tipo);

        Artista artista = artistaService.updatePartialV1(id, dto);

        return ResponseEntity.ok(artista);
    }

    @Operation(
            summary = "Atualizar Artista",
            description = "Atualiza os campos do artista (nome, nacionalidade e tipo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista atualizado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Artista> updateArtistaV1(

            @Parameter(description = "ID do artista", required = true)
            @PathVariable UUID id,

            @Parameter(description = "Novo nome do artista", example = "Elton John")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Novo nacionalidade", example = "Americano")
            @RequestParam(required = false) String nacionalidade,

            @Parameter(description = "Novo tipo", example = "CANTOR")
            @RequestParam(required = false) String tipo

    ) {

        ArtistaRequestDTO dto = new ArtistaRequestDTO(nome, nacionalidade, tipo);

        Artista artista = artistaService.updateV1(id, dto);

        return ResponseEntity.ok(artista);
    }

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
