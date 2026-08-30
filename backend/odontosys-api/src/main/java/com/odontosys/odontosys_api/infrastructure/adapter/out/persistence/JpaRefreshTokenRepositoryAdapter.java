package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.RefreshTokenJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataRefreshTokenRepository;

@Component
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final SpringDataRefreshTokenRepository repository;

    public JpaRefreshTokenRepositoryAdapter(SpringDataRefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(UUID id, UUID userId, String tokenHash, long expirationMs) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setTokenHash(tokenHash);
        entity.setExpiresAt(Instant.now().plusMillis(expirationMs));
        repository.save(entity);
    }

    @Override
    public Optional<RefreshTokenData> findByTokenHash(String tokenHash) {
        return repository.findByTokenHash(tokenHash)
                .map(e -> new RefreshTokenData(
                        e.getId(),
                        e.getUserId(),
                        e.getTokenHash(),
                        e.getExpiresAt(),
                        e.getRevokedAt()
                ));
    }

    @Override
    @Transactional
    public void revokeByTokenHash(String tokenHash) {
        repository.findByTokenHash(tokenHash).ifPresent(entity -> {
            entity.setRevokedAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UUID userId) {
        List<RefreshTokenJpaEntity> tokens = repository.findByUserId(userId);
        Instant now = Instant.now();
        tokens.forEach(token -> {
            if (token.getRevokedAt() == null) {
                token.setRevokedAt(now);
            }
        });
        repository.saveAll(tokens);
    }
}
