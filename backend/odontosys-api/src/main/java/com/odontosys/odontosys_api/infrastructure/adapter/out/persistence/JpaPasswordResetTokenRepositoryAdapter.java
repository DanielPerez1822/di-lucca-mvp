package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.PasswordResetToken;
import com.odontosys.odontosys_api.domain.port.out.PasswordResetTokenRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PasswordResetTokenJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataPasswordResetTokenRepository;

@Component
public class JpaPasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final SpringDataPasswordResetTokenRepository repository;

    public JpaPasswordResetTokenRepositoryAdapter(SpringDataPasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public PasswordResetToken save(PasswordResetToken resetToken) {
        PasswordResetTokenJpaEntity entity = toJpa(resetToken);
        PasswordResetTokenJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<PasswordResetToken> findLatestByEmail(String email) {
        return repository.findFirstByEmailOrderByCreatedAtDesc(email)
                .map(this::toDomain);
    }

    @Override
    @Transactional
    public void invalidateAllByEmail(String email) {
        List<PasswordResetTokenJpaEntity> tokens = repository.findByEmailAndUsedFalse(email);
        tokens.forEach(t -> t.setUsed(true));
        repository.saveAll(tokens);
    }

    private PasswordResetTokenJpaEntity toJpa(PasswordResetToken domain) {
        PasswordResetTokenJpaEntity entity = new PasswordResetTokenJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setCode(domain.getCode());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUsed(domain.isUsed());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    private PasswordResetToken toDomain(PasswordResetTokenJpaEntity entity) {
        return PasswordResetToken.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getCode(),
                entity.getExpiresAt(),
                entity.isUsed(),
                entity.getCreatedAt()
        );
    }
}
