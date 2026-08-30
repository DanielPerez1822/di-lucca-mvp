package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

public record AuthResponseDto(
        UserResponseDto user,
        String accessToken,
        String refreshToken
) {
}
