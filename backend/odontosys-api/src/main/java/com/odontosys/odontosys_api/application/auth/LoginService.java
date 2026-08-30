package com.odontosys.odontosys_api.application.auth;

import java.util.UUID;
import com.odontosys.odontosys_api.application.auth.command.LoginCommand;
import com.odontosys.odontosys_api.application.auth.response.AuthResponse;
import com.odontosys.odontosys_api.application.auth.response.UserResponse;
import com.odontosys.odontosys_api.domain.exception.InvalidCredentialsException;
import com.odontosys.odontosys_api.domain.exception.UserDisabledException;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.LoginUseCase;
import com.odontosys.odontosys_api.domain.port.out.PasswordEncoderPort;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.TokenProviderPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;
    private final long refreshTokenExpirationMs;

    public LoginService(UserRepositoryPort userRepository,
                        RefreshTokenRepositoryPort refreshTokenRepository,
                        PasswordEncoderPort passwordEncoder,
                        TokenProviderPort tokenProvider,
                        long refreshTokenExpirationMs) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    public AuthResponse execute(LoginCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (!user.canLogin()) {
            throw new UserDisabledException();
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String rawRefreshToken = tokenProvider.generateRefreshToken();
        String refreshTokenHash = tokenProvider.hashRefreshToken(rawRefreshToken);

        refreshTokenRepository.save(
                UUID.randomUUID(),
                user.getId(),
                refreshTokenHash,
                refreshTokenExpirationMs
        );

        return new AuthResponse(
                UserResponse.fromDomain(user),
                accessToken,
                rawRefreshToken
        );
    }
}
