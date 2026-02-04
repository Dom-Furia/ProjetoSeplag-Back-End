package com.seplag.api.controller;

import com.seplag.api.dto.RegionalResponseDTO;
import com.seplag.api.service.RegionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regionais")
public class RegionalController {
    private final RegionalService regionalService;

    public RegionalController(RegionalService regionalService) {
        this.regionalService = regionalService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> sincronizar() {
        regionalService.sincronizarRegionais();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RegionalResponseDTO>> listar(){
        return ResponseEntity.ok(regionalService.listarTodas());
    }

}
