package com.odontosys.odontosys_api.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.odontosys.odontosys_api.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;

@Component
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository userRepository;
    private final UserPersistenceMapper mapper;

    public JpaUserRepositoryAdapter(SpringDataUserRepository userRepository, UserPersistenceMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity savedEntity = userRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return userRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
