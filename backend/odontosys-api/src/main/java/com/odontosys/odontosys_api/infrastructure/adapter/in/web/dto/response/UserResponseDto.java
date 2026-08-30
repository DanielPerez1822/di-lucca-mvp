package com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.model.Role;

public record UserResponseDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String documentNumber,
        boolean active,
        Set<Role> roles,
        Instant createdAt
) {
    public static UserResponseDto fromApplication(UserResponse response) {
        return new UserResponseDto(
                response.id(),
                response.email(),
                response.firstName(),
                response.lastName(),
                response.phone(),
                response.documentNumber(),
                response.active(),
                response.roles(),
                response.createdAt()
        );
    }
}
