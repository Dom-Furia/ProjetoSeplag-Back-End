package com.seplag.api.dto;

public record ApiError(
        int status,
        String message
) {

}
