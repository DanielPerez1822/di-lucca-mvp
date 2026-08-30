package com.odontosys.odontosys_api.application.auth;

import java.util.UUID;
import com.odontosys.odontosys_api.domain.port.in.auth.LogoutUseCase;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;

public class LogoutService implements LogoutUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepository;

    public LogoutService(RefreshTokenRepositoryPort refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void execute(UUID userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }
}
