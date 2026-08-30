package com.odontosys.odontosys_api.domain.port.out;

import java.util.Optional;

import com.odontosys.odontosys_api.domain.model.PasswordResetToken;

/**
 * Puerto Secundario — Persistencia de tokens de restablecimiento de contraseña.
 */
public interface PasswordResetTokenRepositoryPort {

    PasswordResetToken save(PasswordResetToken resetToken);

    Optional<PasswordResetToken> findLatestByEmail(String email);

    void invalidateAllByEmail(String email);
}
