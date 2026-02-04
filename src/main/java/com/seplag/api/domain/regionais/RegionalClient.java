package com.seplag.api.domain.regionais;

import com.seplag.api.dto.RegionalDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class RegionalClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL =
            "https://integrador-argus-api.geia.vip/v1/regionais";

    public List<RegionalDTO> buscarRegionais() {
        ResponseEntity<RegionalDTO[]> response =
                restTemplate.getForEntity(URL, RegionalDTO[].class);

        assert response.getBody() != null;
        return Arrays.asList(response.getBody());
    }
}
