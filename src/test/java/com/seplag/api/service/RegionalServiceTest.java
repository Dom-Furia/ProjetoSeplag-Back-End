package com.seplag.api.service;

import com.seplag.api.domain.regionais.Regional;
import com.seplag.api.domain.regionais.RegionalClient;
import com.seplag.api.dto.RegionalDTO;
import com.seplag.api.dto.RegionalResponseDTO;
import com.seplag.api.repositories.RegionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionalServiceTest {
    @Mock
    private RegionalRepository repository;

    @Mock
    private RegionalClient client;

    @InjectMocks
    private RegionalService service;

    @Test
    @DisplayName("Deve Inativar Regional que não vier da API ")
    void deveInativarRegionalQuandoNaoVierDaApi() {

        Regional interna = new Regional();
        interna.setIdExterno(1);
        interna.setNome("Regional A");
        interna.setAtivo(true);

        when(client.buscarRegionais()).thenReturn(List.of());
        when(repository.findByAtivoTrue()).thenReturn(List.of(interna));

        service.sincronizarRegionais();

        assertFalse(interna.getAtivo());
        verify(repository).save(interna);
    }

    @Test
    @DisplayName("Deve  Criar Nova Regional Quando o Nome for Alterado e Inativar a Antiga")
    void deveCriarNovaRegionalQuandoNomeForAlterado() {
        // Arrange
        Regional interna = new Regional();
        interna.setIdExterno(1);
        interna.setNome("Antigo Nome");
        interna.setAtivo(true);

        RegionalDTO externa = new RegionalDTO(1, "Novo Nome");

        when(client.buscarRegionais()).thenReturn(List.of(externa));
        when(repository.findByAtivoTrue()).thenReturn(List.of(interna));

        service.sincronizarRegionais();

        assertFalse(interna.getAtivo());

        verify(repository).save(interna);
        verify(repository).save(argThat(r ->
                r.getIdExterno().equals(1) &&
                        r.getNome().equals("Novo Nome") &&
                        r.getAtivo()
        ));
    }

    @Test
    @DisplayName("Deve Inserir Nova Regional Quando Não Existir Internamente")
    void deveInserirNovaRegionalQuandoNaoExistirInternamente() {

        RegionalDTO externa = new RegionalDTO(2, "Regional Nova");

        when(client.buscarRegionais()).thenReturn(List.of(externa));
        when(repository.findByAtivoTrue()).thenReturn(List.of());


        service.sincronizarRegionais();


        verify(repository).save(argThat(r ->
                r.getIdExterno().equals(2) &&
                        r.getNome().equals("Regional Nova") &&
                        r.getAtivo()
        ));
    }

    @Test
    @DisplayName("Não Deve Fazer Nada Quando Não Houver Mudanças")
    void naoDeveFazerNadaQuandoNaoHouverMudancas() {
        Regional interna = new Regional();
        interna.setIdExterno(1);
        interna.setNome("Regional A");
        interna.setAtivo(true);

        RegionalDTO externa = new RegionalDTO(1, "Regional A");

        when(client.buscarRegionais()).thenReturn(List.of(externa));
        when(repository.findByAtivoTrue()).thenReturn(List.of(interna));

        service.sincronizarRegionais();

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve Listar Todas as Regionais")
    void deveListarTodasAsRegionais() {
        Regional r1 = new Regional();
        r1.setId(1L);
        r1.setIdExterno(10);
        r1.setNome("Regional 1");
        r1.setAtivo(true);

        when(repository.findAll()).thenReturn(List.of(r1));

        List<RegionalResponseDTO> result = service.listarTodas();

        assertEquals(1, result.size());
        assertEquals("Regional 1", result.get(0).nome());
    }






}