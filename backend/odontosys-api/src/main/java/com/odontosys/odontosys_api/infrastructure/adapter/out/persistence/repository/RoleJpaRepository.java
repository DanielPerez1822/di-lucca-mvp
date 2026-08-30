package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
    Optional<RoleJpaEntity> findByName(String name);
}
