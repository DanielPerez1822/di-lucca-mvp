package com.odontosys.odontosys_api.domain.port.out;

import java.util.Optional;
import java.util.UUID;

/**
 * Puerto secundario — Contrato de persistencia para refresh tokens.
 */
public interface RefreshTokenRepositoryPort {

    void save(UUID id, UUID userId, String tokenHash, long expirationMs);

    Optional<RefreshTokenData> findByTokenHash(String tokenHash);

    void revokeByTokenHash(String tokenHash);

    void revokeAllByUserId(UUID userId);

    /**
     * Datos del refresh token almacenado.
     */
    record RefreshTokenData(
            UUID id,
            UUID userId,
            String tokenHash,
            java.time.Instant expiresAt,
            java.time.Instant revokedAt
    ) {
        public boolean isExpired() {
            return java.time.Instant.now().isAfter(expiresAt);
        }

        public boolean isRevoked() {
            return revokedAt != null;
        }

        public boolean isValid() {
            return !isExpired() && !isRevoked();
        }
    }
}
