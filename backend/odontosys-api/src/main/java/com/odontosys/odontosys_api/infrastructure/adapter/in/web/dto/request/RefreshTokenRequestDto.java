package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken
) {
}
