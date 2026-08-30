package com.odontosys.odontosys_api.application.auth;

import com.odontosys.odontosys_api.application.auth.command.RequestPasswordResetCommand;
import com.odontosys.odontosys_api.domain.model.PasswordResetToken;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.auth.RequestPasswordResetUseCase;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.PasswordResetTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class RequestPasswordResetService implements RequestPasswordResetUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordResetTokenRepositoryPort resetTokenRepository;
    private final EmailSenderPort emailSender;
    private final int codeExpirationMinutes;

    public RequestPasswordResetService(
            UserRepositoryPort userRepository,
            PasswordResetTokenRepositoryPort resetTokenRepository,
            EmailSenderPort emailSender,
            int codeExpirationMinutes
    ) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.emailSender = emailSender;
        this.codeExpirationMinutes = codeExpirationMinutes;
    }

    @Override
    public void execute(RequestPasswordResetCommand command) {
        userRepository.findByEmail(command.email()).ifPresent(user -> {
            if (user.canLogin()) {
                // Invalidate existing tokens for this email
                resetTokenRepository.invalidateAllByEmail(user.getEmail());

                // Create new reset token
                PasswordResetToken token = PasswordResetToken.create(user.getEmail(), codeExpirationMinutes);
                resetTokenRepository.save(token);

                // Send verification code email
                emailSender.sendPasswordResetCode(user.getEmail(), token.getCode(), codeExpirationMinutes);
            }
        });
        // Note: For security reasons, if user doesn't exist, we silently complete without leaking user existence.
    }
}
