package com.odontosys.odontosys_api.application.auth;

import com.odontosys.odontosys_api.application.auth.command.ResetPasswordCommand;
import com.odontosys.odontosys_api.domain.exception.InvalidResetCodeException;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.PasswordResetToken;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.ResetPasswordUseCase;
import com.odontosys.odontosys_api.domain.port.out.PasswordEncoderPort;
import com.odontosys.odontosys_api.domain.port.out.PasswordResetTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class ResetPasswordService implements ResetPasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordResetTokenRepositoryPort resetTokenRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final PasswordEncoderPort passwordEncoder;

    public ResetPasswordService(
            UserRepositoryPort userRepository,
            PasswordResetTokenRepositoryPort resetTokenRepository,
            RefreshTokenRepositoryPort refreshTokenRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(ResetPasswordCommand command) {
        PasswordResetToken resetToken = resetTokenRepository.findLatestByEmail(command.email())
                .orElseThrow(() -> new InvalidResetCodeException("Código de verificación no encontrado o inválido"));

        if (!resetToken.isValid(command.code())) {
            throw new InvalidResetCodeException("Código de verificación incorrecto o expirado");
        }

        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Update password with new encoded password
        String newPasswordHash = passwordEncoder.encode(command.newPassword());
        user.updatePassword(newPasswordHash);
        userRepository.save(user);

        // Mark token as used
        resetToken.markAsUsed();
        resetTokenRepository.save(resetToken);

        // Revoke all active sessions for security
        refreshTokenRepository.revokeAllByUserId(user.getId());
    }
}
