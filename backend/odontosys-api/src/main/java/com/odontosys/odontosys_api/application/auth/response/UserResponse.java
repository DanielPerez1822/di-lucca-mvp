package com.odontosys.odontosys_api.application.auth.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.model.User;

public record UserResponse(
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
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getDocumentNumber(),
                user.isActive(),
                user.getRoles(),
                user.getCreatedAt()
        );
    }
}
