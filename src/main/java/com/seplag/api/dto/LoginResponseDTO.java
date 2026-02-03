package com.seplag.api.dto;

public record LoginResponseDTO(
        String nome,
        String accessToken,
        String refreshToken
) {
}
