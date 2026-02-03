package com.seplag.api.dto;

import java.util.UUID;

public record UserRequestDTO(
         String name,
         String email,
         String password
) {
}
