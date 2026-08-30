package com.odontosys.odontosys_api.application.auth;

import java.util.UUID;
import com.odontosys.odontosys_api.application.auth.command.RefreshTokenCommand;
import com.odontosys.odontosys_api.application.auth.response.TokenResponse;
import com.odontosys.odontosys_api.domain.exception.InvalidTokenException;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.RefreshTokenUseCase;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort.RefreshTokenData;
import com.odontosys.odontosys_api.domain.port.out.TokenProviderPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;
    private final long refreshTokenExpirationMs;

    public RefreshTokenService(RefreshTokenRepositoryPort refreshTokenRepository,
                               UserRepositoryPort userRepository,
                               TokenProviderPort tokenProvider,
                               long refreshTokenExpirationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    public TokenResponse execute(RefreshTokenCommand command) {
        String tokenHash = tokenProvider.hashRefreshToken(command.refreshToken());
        RefreshTokenData tokenData = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Refresh token no encontrado o inválido"));

        if (!tokenData.isValid()) {
            throw new InvalidTokenException("Refresh token expirado o revocado");
        }

        User user = userRepository.findById(tokenData.userId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!user.canLogin()) {
            throw new InvalidTokenException("La cuenta del usuario está desactivada");
        }

        // Revoke used token (token rotation)
        refreshTokenRepository.revokeByTokenHash(tokenHash);

        // Generate new token pair
        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRawRefreshToken = tokenProvider.generateRefreshToken();
        String newRefreshTokenHash = tokenProvider.hashRefreshToken(newRawRefreshToken);

        refreshTokenRepository.save(
                UUID.randomUUID(),
                user.getId(),
                newRefreshTokenHash,
                refreshTokenExpirationMs
        );

        return new TokenResponse(newAccessToken, newRawRefreshToken);
    }
}
