package com.seplag.api.controller;

import com.seplag.api.dto.RegionalResponseDTO;
import com.seplag.api.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
@RequiredArgsConstructor
@Tag(
        name = "Regionais V1",
        description = "Endpoints responsáveis pela sincronização e consulta de regionais"
)
public class RegionalController {

    private final RegionalService regionalService;

    @Operation(
            summary = "Sincronizar regionais",
            description = "Realiza a sincronização das regionais a partir da fonte externa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sincronização realizada com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Erro ao sincronizar regionais")
            }
    )
    @PostMapping("/sync")
    public ResponseEntity<String> sincronizar() {
        regionalService.sincronizarRegionais();
        return ResponseEntity.ok("Sincronizado com sucesso.");
    }

    @Operation(
            summary = "Listar regionais",
            description = "Retorna todas as regionais cadastradas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar regionais")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<List<RegionalResponseDTO>> listar(){
        return ResponseEntity.ok(regionalService.listarTodas());
    }
}
