package com.seplag.api.dto;

import java.util.UUID;

public record UserRequestDTO(
         UUID id,
         String name,
         String email,
         String password
) {
}
