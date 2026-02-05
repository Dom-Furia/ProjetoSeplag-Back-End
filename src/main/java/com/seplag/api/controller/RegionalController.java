package com.seplag.api.controller;

import com.seplag.api.dto.RegionalResponseDTO;
import com.seplag.api.service.RegionalService;
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
public class RegionalController {

    private final RegionalService regionalService;


    @PostMapping("/sync")
    public ResponseEntity<String> sincronizar() {
        regionalService.sincronizarRegionais();
        return ResponseEntity.ok("Sincronizado com sucesso.");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RegionalResponseDTO>> listar(){
        return ResponseEntity.ok(regionalService.listarTodas());
    }

}
