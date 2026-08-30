package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.ProcedureJpaEntity;

public interface SpringDataProcedureRepository extends JpaRepository<ProcedureJpaEntity, UUID> {

    Optional<ProcedureJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
