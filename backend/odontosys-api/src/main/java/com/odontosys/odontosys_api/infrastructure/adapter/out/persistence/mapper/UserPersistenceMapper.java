package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.RoleJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.RoleJpaRepository;

@Component
public class UserPersistenceMapper {

    private final RoleJpaRepository roleJpaRepository;

    public UserPersistenceMapper(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Set<Role> domainRoles = entity.getRoles().stream()
                .map(r -> Role.valueOf(r.getName()))
                .collect(Collectors.toSet());

        return User.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getDocumentNumber(),
                entity.isActive(),
                domainRoles,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public UserJpaEntity toJpa(User domain) {
        if (domain == null) {
            return null;
        }
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setPhone(domain.getPhone());
        entity.setDocumentNumber(domain.getDocumentNumber());
        entity.setActive(domain.isActive());

        Set<RoleJpaEntity> jpaRoles = domain.getRoles().stream()
                .map(roleEnum -> roleJpaRepository.findByName(roleEnum.name())
                        .orElseGet(() -> roleJpaRepository.save(new RoleJpaEntity(UUID.randomUUID(), roleEnum.name(), "Rol de sistema " + roleEnum.name()))))
                .collect(Collectors.toSet());

        entity.setRoles(jpaRoles);
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeletedAt(domain.getDeletedAt());
        return entity;
    }
}
