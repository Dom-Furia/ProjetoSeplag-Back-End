package com.seplag.api.controller;

import com.seplag.api.security.SecurityConfig;
import com.seplag.api.service.MinioStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@Tag(name = "Files", description = "Endpoints responsáveis por gerar links pre-assinados com expiração")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class FileController {

    private final MinioStorageService storageService;

    public FileController(MinioStorageService storageService) {
        this.storageService = storageService;
    }

    @Operation(
            summary = "Gerar Link para Dowload da Imagem do Album",
            description = "Criar link informando nome do arquivo de imagem"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Link gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possivel gerar link"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
        @GetMapping("/download/{fileName}")
        public ResponseEntity<String> getDownloadLink(@PathVariable String fileName) {
            String url = storageService.generateUrl(fileName);
            return ResponseEntity.ok(url);
        }

}
