package com.odontosys.odontosys_api.application.auth.response;

public record AuthResponse(
        UserResponse user,
        String accessToken,
        String refreshToken
) {
}
