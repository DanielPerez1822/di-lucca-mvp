package com.odontosys.odontosys_api.application.auth.command;

import java.util.Set;
import com.odontosys.odontosys_api.domain.model.Role;

public record CreateUserCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String documentNumber,
        Set<Role> roles
) {
}
