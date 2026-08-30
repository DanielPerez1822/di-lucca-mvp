package com.odontosys.odontosys_api.application.auth;

import com.odontosys.odontosys_api.application.auth.command.CreateUserCommand;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.exception.UserAlreadyExistsException;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.CreateUserUseCase;
import com.odontosys.odontosys_api.domain.port.out.PasswordEncoderPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class CreateUserService implements CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserService(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse execute(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException("El email " + command.email() + " ya está registrado");
        }

        if (command.documentNumber() != null && !command.documentNumber().isBlank()
                && userRepository.existsByDocumentNumber(command.documentNumber())) {
            throw new UserAlreadyExistsException("El número de documento " + command.documentNumber() + " ya está registrado");
        }

        String passwordHash = passwordEncoder.encode(command.password());

        User user = User.create(
                command.email(),
                passwordHash,
                command.firstName(),
                command.lastName(),
                command.phone(),
                command.documentNumber(),
                command.roles()
        );

        User savedUser = userRepository.save(user);
        return UserResponse.fromDomain(savedUser);
    }
}
