package com.odontosys.odontosys_api.application.auth;

import java.util.UUID;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.GetCurrentUserUseCase;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class GetCurrentUserService implements GetCurrentUserUseCase {

    private final UserRepositoryPort userRepository;

    public GetCurrentUserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
        return UserResponse.fromDomain(user);
    }
}
