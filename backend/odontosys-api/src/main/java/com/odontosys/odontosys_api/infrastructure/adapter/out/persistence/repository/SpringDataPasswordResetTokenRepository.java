package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.PasswordResetTokenJpaEntity;

public interface SpringDataPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenJpaEntity, UUID> {

    Optional<PasswordResetTokenJpaEntity> findFirstByEmailOrderByCreatedAtDesc(String email);

    List<PasswordResetTokenJpaEntity> findByEmailAndUsedFalse(String email);
}
