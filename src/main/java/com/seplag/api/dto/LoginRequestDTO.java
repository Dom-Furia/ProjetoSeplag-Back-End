package com.seplag.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO (
        @Schema(example = "joao@test.com")
        String email,

        @Schema(example = "test@123")
        String password){

}
