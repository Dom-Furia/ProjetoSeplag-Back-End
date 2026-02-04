package com.seplag.api.service;

import com.seplag.api.domain.regionais.Regional;
import com.seplag.api.domain.regionais.RegionalClient;
import com.seplag.api.dto.RegionalDTO;
import com.seplag.api.dto.RegionalResponseDTO;
import com.seplag.api.repositories.RegionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class RegionalService {
    private final RegionalRepository repository;
    private final RegionalClient client;

    public RegionalService(RegionalRepository repository, RegionalClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Transactional
    public void sincronizarRegionais() {

        List<RegionalDTO> externas = client.buscarRegionais();
        List<Regional> internasAtivas = repository.findByAtivoTrue();

        // Mapa para acesso rápido
        Map<Integer, RegionalDTO> externasMap = externas.stream()
                .collect(Collectors.toMap(RegionalDTO::id, Function.identity()));

        //  Inativar ausentes ou alteradas
        for (Regional interna : internasAtivas) {

            RegionalDTO externa = externasMap.get(interna.getIdExterno());

            if (externa == null) {
                // Não veio no endpoint → inativar
                interna.setAtivo(false);
                repository.save(interna);
                continue;
            }

            if (!interna.getNome().equals(externa.nome())) {
                // Alterado → inativa antigo
                interna.setAtivo(false);
                repository.save(interna);

                // Cria novo
                repository.save(novaRegional(externa));
            }
        }

        // Inserir novos
        for (RegionalDTO externa : externas) {
            boolean existe = internasAtivas.stream()
                    .anyMatch(r -> r.getIdExterno().equals(externa.id()));

            if (!existe) {
                repository.save(novaRegional(externa));
            }
        }
    }

    private Regional novaRegional(RegionalDTO dto) {
        Regional r = new Regional();
        r.setIdExterno(dto.id());
        r.setNome(dto.nome());
        r.setAtivo(true);
        return r;
    }


    public List<RegionalResponseDTO> listarTodas() {

        return repository.findAll()
                .stream()
                .map(regional -> new RegionalResponseDTO(
                        regional.getId(),
                        regional.getIdExterno(),
                        regional.getNome(),
                        regional.getAtivo()
                )).toList();
    }
}
