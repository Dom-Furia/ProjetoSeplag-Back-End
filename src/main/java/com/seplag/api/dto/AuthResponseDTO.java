package com.seplag.api.dto;

public record AuthResponseDTO(
        String nome,
        String accessToken,
        String refreshToken
) {
}
